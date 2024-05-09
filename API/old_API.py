from fastapi import FastAPI, HTTPException
from typing import Dict
import boto3
import cv2
import tensorflow as tf
import numpy as np

app = FastAPI()

# Initialize the S3 client
s3 = boto3.client('s3', aws_access_key_id="", aws_secret_access_key="")

# Load the TensorFlow model
model = None  # Load the model here (once) for better performance

def load_model():
    global model
    if model is None:
        model = tf.keras.models.load_model("/home/ec2-user/API/API/model.h5")

load_model()  # Load the model at startup

@app.post("/predict/")
async def predict(data: Dict[str, str]):
    try:
        # Extract the S3 access point and image object key from the input data
        s3_access_point = data.get("s3_access_point")
        image_object_key = data.get("image_object_key")

        if not s3_access_point or not image_object_key:
            raise HTTPException(status_code=400, detail="S3 access point and image object key are required.")

        # Read the image data from S3
        image_data = s3.get_object(Bucket=s3_access_point, Key=image_object_key)['Body'].read()

        # Decode the image
        image = cv2.imdecode(np.frombuffer(image_data, np.uint8), cv2.IMREAD_COLOR)
        image = cv2.resize(image, (640, 640))
        image = np.expand_dims(image, axis=0)

        # Make predictions using the loaded model
        prediction = model.predict(image)

        return {"prediction": prediction.tolist()}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))