from fastapi import FastAPI, File, UploadFile
from starlette.responses import JSONResponse
from typing import List
import cv2
import tensorflow as tf
import numpy as np
import tempfile

app = FastAPI()


model_path="/Users/mohammedk/CSCI 5308/resnet.h5"
@app.post("/predict/")
async def predict(files: List[UploadFile]):
    try:
        loaded_model = tf.keras.models.load_model(model_path)  

        predictions = []

        for file in files:
            with tempfile.NamedTemporaryFile(delete=False) as temp_file:
       
                temp_file.write(file.file.read())


            image = cv2.imread(temp_file.name)
            image = cv2.resize(image, (640, 640))
            image = np.expand_dims(image, axis=0)


            prediction = loaded_model.predict(image)

            predictions.append({"filename": file.filename, "prediction": prediction.tolist()})

        return JSONResponse(content={"predictions": predictions}, status_code=200)

    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)
