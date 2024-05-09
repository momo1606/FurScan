from fastapi import FastAPI, HTTPException
from typing import Dict
import boto3
import cv2
import tensorflow as tf
import numpy as np
import pymysql
from createPdf import createPdf
from datetime import datetime
import os
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()
app.add_middleware( CORSMiddleware, allow_origins=["*"], allow_credentials=True, allow_methods=["*"], allow_headers=["*"])

# Initialize the S3 client
s3 = boto3.client('s3', aws_access_key_id="", aws_secret_access_key="")

# Load the TensorFlow model
model = None  # Load the model here (once) for better performance

def load_model():
    global model
    if model is None:
        model = tf.keras.models.load_model("/home/ubuntu/resnet.h5")


def update_report_text(pet_report_id, new_report_text):
    try:
        # Database connection information
        db_config = {
            "host": "furscan-project-1.cirqdoltkjgp.us-east-1.rds.amazonaws.com",
            "user": "admin",
            "password": "FURscan123#$",
            "database": "FURSCAN",
        }

        # Establish a connection to the database
        connection = pymysql.connect(**db_config)

        # Create a cursor object
        cursor = connection.cursor()

        # SQL statement to update the report_text
        update_query = "UPDATE pet_report SET report_text = %s WHERE pet_report_id = %s"

        # Execute the update query with the provided parameters
        cursor.execute(update_query, (new_report_text, pet_report_id))

        # Commit the changes to the database
        connection.commit()

        # Close the cursor and database connection
        cursor.close()
        connection.close()

        return "Report text updated successfully"

    except pymysql.MySQLError as e:
        return f"Error: {str(e)}"

def update_predicted_disease(pet_report_id, predicted_disease_name):
    try:
        # Database connection information
        db_config = {
            "host": "furscan-project-1.cirqdoltkjgp.us-east-1.rds.amazonaws.com",
            "user": "admin",
            "password": "FURscan123#$",
            "database": "FURSCAN",
        }

        # Establish a connection to the database
        connection = pymysql.connect(**db_config)

        # Create a cursor object
        cursor = connection.cursor()

        # SQL statement to update the disease
        update_query = "UPDATE pet_report SET disease = %s WHERE pet_report_id = %s"

        # Execute the update query with the provided parameters
        cursor.execute(update_query, (predicted_disease_name, pet_report_id))

        # Commit the changes to the database
        connection.commit()

        # Close the cursor and database connection
        cursor.close()
        connection.close()

        return "Predicted Disease updated successfully"

    except pymysql.MySQLError as e:
        return f"Error: {str(e)}"

def get_pet_info(pet_report_id):
    # Define your database connection parameters
    db_config = {
        'host': 'furscan-project-1.cirqdoltkjgp.us-east-1.rds.amazonaws.com',
        'user': 'admin',
        'password': 'FURscan123#$',
        'database': 'FURSCAN',
    }

    # Initialize the connection and cursor
    connection = pymysql.connect(**db_config)
    cursor = connection.cursor()

    try:
        # Query to retrieve pet information
        query = """
            SELECT 
                pr.pet_name, pr.age, pr.breed,
                CONCAT(u.first_name, ' ', u.last_name) AS owner_name
            FROM pet_report pr
            INNER JOIN mst_users u ON pr.user_id = u.user_id
            WHERE pr.pet_report_id = %s
        """

        # Execute the query
        cursor.execute(query, (pet_report_id,))
        
        # Fetch the result
        result = cursor.fetchone()

        if result:
            pet_name, age, breed, owner_name = result
            return {
                'pet_name': pet_name,
                'age': age,
                'breed': breed,
                'owner_name': owner_name,
            }
        else:
            return None  # Pet report not found

    except pymysql.Error as e:
        # Handle any database errors here
        print(f"Error: {e}")
        return None

    finally:
        # Close the cursor and database connection
        cursor.close()
        connection.close()

def get_remark_pdf(pet_report_id):
    # Define your database connection parameters
    db_config = {
        'host': 'furscan-project-1.cirqdoltkjgp.us-east-1.rds.amazonaws.com',
        'user': 'admin',
        'password': 'FURscan123#$',
        'database': 'FURSCAN',
    }

    # Initialize the connection and cursor
    connection = pymysql.connect(**db_config)
    cursor = connection.cursor()

    try:
        # Query to retrieve pet information
        query = """
            SELECT report_text, remarks FROM FURSCAN.pet_report where pet_report_id = %s
        """

        # Execute the query
        cursor.execute(query, (pet_report_id,))
        
        # Fetch the result
        result = cursor.fetchone()

        if result:
            report_text, remarks = result
            return {
                'report_text': str(report_text),
                'remarks': str(remarks),
            }
        else:
            return None  # Pet report not found

    except pymysql.Error as e:
        # Handle any database errors here
        print(f"Error: {e}")
        return None

    finally:
        # Close the cursor and database connection
        cursor.close()
        connection.close()

def get_generic_precautions_medication(disease_name):
    try:
        # Establish a connection to the MySQL database
        connection = pymysql.connect(
            host='furscan-project-1.cirqdoltkjgp.us-east-1.rds.amazonaws.com',
            user='admin',
            password='FURscan123#$',
            database='FURSCAN',
            cursorclass=pymysql.cursors.DictCursor
        )

        with connection.cursor() as cursor:
            # Define the SQL query to retrieve the data
            query = "SELECT generic_precautions_medication FROM disease WHERE name = %s"

            # Execute the query with the provided disease name
            cursor.execute(query, (disease_name,))

            # Fetch the result
            result = cursor.fetchone()

            # Check if the disease name was found in the database
            if result:
                return result["generic_precautions_medication"]
            else:
                return "Disease not found in the database."

    except pymysql.MySQLError as error:
        return f"Error: {error}"

    finally:
        # Close the database connection
        if 'connection' in locals():
            connection.close()
    

load_model()  # Load the model at startup

@app.post("/predict/{pet_report_id}/{objectKey}")
async def predict(pet_report_id: int, objectKey: str):
    try:
        # Extract the S3 access point and image object key from the input data
        s3_access_point = "furscan-hgdzprww3nof8wradxmbi468jiqksuse1a-s3alias"
        image_object_key="dog_images/"+objectKey
        pet_report_id= int(pet_report_id)

        Disease_map = {0:"Bacterial" , 1:"Fungal" , 2:"Hypersensitivity"}
        # image_object_key = data.get("image_object_key")

        if not s3_access_point or not image_object_key:
            raise HTTPException(status_code=400, detail="S3 access point and image object key are required.")

        # Read the image data from S3
        image_data = s3.get_object(Bucket=s3_access_point, Key=image_object_key)['Body'].read()

        # Decode the image
        image = cv2.imdecode(np.frombuffer(image_data, np.uint8), cv2.IMREAD_COLOR)
        source_image = image
        image = cv2.resize(image, (640, 640))
        image = np.expand_dims(image, axis=0)

        # Make predictions using the loaded model
        prediction = model.predict(image)

        list_prediction=prediction.tolist()[0]

        predicted_disease_name = Disease_map[list_prediction.index(max(list_prediction))]

        medication=get_generic_precautions_medication(predicted_disease_name)
        pet_info = get_pet_info(pet_report_id)

        current_timestamp = datetime.now().strftime("%Y%m%d%H%M%S")

        # Create a unique filename by combining pet_report_id and current_timestamp
        unique_filename = "pdf_reports/"+f"pet_report_{pet_report_id}_{current_timestamp}.pdf"

        c = createPdf(source_image, predicted_disease_name, medication)
        c.createPDF(s3= s3, s3_access_point= s3_access_point, name=pet_info['pet_name'], age=pet_info['age'], breed=pet_info['breed'], owner=pet_info['owner_name'], unique_filename= unique_filename)

        update_predicted_disease(pet_report_id= pet_report_id, predicted_disease_name= predicted_disease_name)
        update_report_text(pet_report_id= pet_report_id, new_report_text= unique_filename)

        return {"report_object_key": unique_filename}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/add_doctor_remarks/{pet_report_id}")
async def add_doctor_remarks(pet_report_id: int):
    try:
        print(4)
        # Extract the S3 access point and image object key from the input data
        s3_access_point = "furscan-hgdzprww3nof8wradxmbi468jiqksuse1a-s3alias"
        pet_report_id= int(pet_report_id)

        result = get_remark_pdf(pet_report_id)
        report_text = result["report_text"]
        print("REPORT TEXT - "+report_text)
        remarks = result["remarks"]

        local_filename= "temp.pdf"
        local_directory= "/tmp"
        #print(tmp_report)
        try:
            #s3.download_file(s3_access_point, report_text, tmp_report)
            response = s3.get_object(Bucket=s3_access_point, Key=report_text)
            with open(f"{local_directory}/{local_filename}", "wb") as local_file:
                local_file.write(response["Body"].read())
            print("File downloaded successfully.")
        except Exception as e:
            print(f"Error downloading file: {e}")
            return {"Error": "Error downloading file"}

        current_timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        unique_filename = "pdf_reports/"+f"pet_report_{pet_report_id}_{current_timestamp}.pdf"

        c = createPdf(0, 0, 0)
        c.updatePdf(remarks, f"{local_directory}/{local_filename}", s3, s3_access_point, unique_filename)
        update_report_text(pet_report_id= pet_report_id, new_report_text= unique_filename)

        try:
            os.remove(f"{local_directory}/{local_filename}")
            print("downloaded file deleted")
        except:
            pass

        return {"report_object_key": unique_filename}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))