from reportlab.pdfgen.canvas import Canvas
from reportlab.lib.pagesizes import letter, A4
import cv2
from PIL import Image
from io import BytesIO
import os
from datetime import datetime

from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import Paragraph

from PyPDF2 import PdfWriter, PdfReader
import io



class createPdf:

    def __init__(self, image, prediction, medication) -> None:
        if (type(image)!= int):
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            self.image = Image.fromarray(image)
            
            #self.image.show()
            self.prediction = prediction
            self.medication = medication

    def createPDF(self, s3, s3_access_point, name, age, breed, owner, unique_filename):

        buffer = BytesIO()

        #bottomup=0 #"imageId-userId-datetime.pdf"
        canvas = Canvas(buffer) #pagesize=  A4
        
        width, height = A4
        print(width, height)
        textobject = canvas.beginText() 
        canvas.line(7,700,570,700)
        canvas.setFont("Times-Roman", 30)
        canvas.drawCentredString(300, 750, "Fur-Scan")
        canvas.drawInlineImage(self.image,305,520, width = 250, height = 150)
        canvas.setFont("Times-Roman", 12)
        canvas.drawString(50, 250, "Doctor's Notes:")
        canvas.drawString(50, 450, "Generic Precautions and Medication: ")
        textobject.setTextOrigin(50, 430)
        listMedication = self.medication.split(". ")
        for i in listMedication:
            textobject.textLine(text =i+".")

        canvas.drawString(50, 500, "Disease Prediction: "+self.prediction)
        
        textobject.setTextOrigin(50, 650)
        # Set font face and size
        textobject.setFont('Times-Roman', 12)
        # Write a line of text + carriage return
        textobject.textLine(text="Date: "+datetime.now().strftime("%d %B %Y"))
        textobject.textLine(text = "Owner Name: "+owner)
        textobject.textLine(text = "Pet Name: "+name)
        textobject.textLine(text = "Pet age: "+str(age))
        textobject.textLine(text = "Breed: "+breed)
        canvas.drawText(textobject)

        
        # Indicate that you're done with the page
        canvas.showPage()
        canvas.save()
        # Reset the buffer to the beginning
        buffer.seek(0)

        temp_file_path = "/tmp/temp_pdf.pdf"
        # Create a temporary file and save the buffer content
        with open(temp_file_path, "wb") as f:
            f.write(buffer.read())

        s3.upload_file(temp_file_path, s3_access_point, unique_filename)
        try:
            os.remove(temp_file_path)
            print("temp file deleted")
        except:
            pass

    def writeParagraph(self,string, startWidth = 50, startHeight=210):
        packet = io.BytesIO()
        canva = Canvas(packet, pagesize=letter)        
        styleSheet = getSampleStyleSheet()
        style = styleSheet['BodyText']
        P=Paragraph(string,style)
        aW = 510    # available width and height
        aH = 800
        w,h = P.wrap(aW, aH)    # find required space
        if w<=aW and h<=aH:
            P.drawOn(canva,startWidth,startHeight)
            aH = aH - h 
            canva.save()
        return packet
        
    def updatePdf(self, doctorNote, pdfPath, s3, s3_access_point, unique_filename):
        packet = self.writeParagraph(doctorNote)
        packet.seek(0)        
        new_pdf = PdfReader(packet)
        # read your existing PDF
        existing_pdf = PdfReader(open(pdfPath, "rb"))
        output = PdfWriter()
        page = existing_pdf.pages[0]
        page.merge_page(new_pdf.pages[0])
        output.add_page(page)
        # finally, write "output" to a real file
        temp_file_path = "/tmp/destination.pdf"
        outputStream = open(temp_file_path, "wb")
        output.write(outputStream)
        outputStream.close()   
        s3.upload_file(temp_file_path, s3_access_point, unique_filename)
        try:
            os.remove(temp_file_path)
            print("temp file deleted")
        except:
            pass   



if __name__ == "__main__":

    print(str(None))
  