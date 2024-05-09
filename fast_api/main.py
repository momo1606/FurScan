from fastapi import FastAPI, File, UploadFile, Request
import PIL.Image as image
import numpy as np
from fastapi.responses import HTMLResponse
from fastapi.templating import Jinja2Templates

import sys

#sys.path.insert(1, "")

from machine_learning.predict import Prediction

image_global = {"image": image, "result": None}

predict = Prediction("__path_to_model__")
app = FastAPI()
templates = Jinja2Templates(directory="templates")

@app.get("/upload/", response_class=HTMLResponse)
async def upload(request: Request):
   return templates.TemplateResponse("uploadfile.html", {"request": request})

@app.post("/uploader/")
async def create_upload_file(file: UploadFile = File(...)):
    with image.open(file.file) as im:
        print(im.mode)
        im = im.convert('RGB')
        im = np.asarray(im)
        image_global["image"] = image
        print(im, im.shape)
    pred = predict.predict(im)
    #image_global["result"] = pred
    max_pred = max(pred[0])
    max_pred = np.where(pred == max_pred)
    if max_pred == 0:
        return {"result" : "fungus"}
    elif max_pred == 1:
        return {"result" : "bacteeria"}
    else:
        return {"result": "hypersensitivity"}
    #return {"filename": file.filename}

"""def predict_image(image):
    pred = image_global["result"][0]
    max_pred = max(pred)
    max_pred = np.where(pred == max_pred)
    if max_pred == 0:
        return {"result" : "fungus"}
    elif max_pred == 1:
        return {"result" : "bacteeria"}
    else:
        return {"result": "hypersensitivity"}


@app.get("/result/")
async def send_result():
    pred = image_global["result"][0]
    max_pred = max(pred)
    max_pred = np.where(pred == max_pred)
    if max_pred == 0:
        return {"result" : "fungus"}
    elif max_pred == 1:
        return {"result" : "bacteeria"}
    else:
        return {"result": "hypersensitivity"}"""
    