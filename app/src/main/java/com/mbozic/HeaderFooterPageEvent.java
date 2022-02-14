package com.mbozic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    private Context context;
    private boolean ispisiJednom = true;

    public HeaderFooterPageEvent(Context context) {
        this.context = context;
    }

    public void onStartPage(PdfWriter writer, Document document) {


        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("SPECIFIKACIJE TRUPACA" ), 50, 30, 0);

        /*try {
            Rectangle rectDoc = document.getPageSize();
            float width = rectDoc.getWidth();
            float height = rectDoc.getHeight();
            float imageStartX = width - document.rightMargin() - 510f;
            float imageStartY = height - document.topMargin() - 80f;

            System.gc();
            InputStream inputStream;
            inputStream = context.getResources().openRawResource(R.raw.logo1);

            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            byte[] byteArray = stream.toByteArray();

            Image img = Image.getInstance(byteArray);

            img.scaleToFit(100,100);
            img.setAbsolutePosition((rectDoc.getLeft() + rectDoc.getRight()) / 2 - 45, rectDoc.getTop() - 50);
            img.setAlignment(Image.TEXTWRAP);
            img.scaleAbsolute(137f, 60f);
            img.setAbsolutePosition(imageStartX, imageStartY); // Adding Image


            if (ispisiJednom) {
                writer.getDirectContent().addImage(img);
                ispisiJednom = false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    public void onEndPage(PdfWriter writer, Document document) {
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("http://www.bozic.hr"), 70, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Stranica " + document.getPageNumber()), 550, 30, 0);
    }

}
