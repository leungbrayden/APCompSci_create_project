package create_project;

import java.util.ArrayList;
import java.util.function.Supplier;

import processing.core.PApplet;
import processing.core.PConstants; 

public class GUI {
  ArrayList<Button> buttons = new ArrayList<>();
  ArrayList<Text> text = new ArrayList<>();
  boolean showBackground = true;
  int textColor;

  GUI() {
    this.textColor = Constants.TEXT_COLOR;
  }

  public void addButton(Button button) {
    button.setTextColor(this.textColor);
    this.buttons.add(button);
  }

  public void addText(Text text) {
    this.text.add(text);
  }
  
  public void noBackground() {
    showBackground = false;
  }
  
  public void textColor(int rgb) {
    this.textColor = rgb;
  }
  
  public void drawScreen(PApplet app) {
    if (showBackground) {
      app.background(Constants.backgroundColor);
    }
    for (int i = 0; i < this.buttons.size(); i++) {
      buttons.get(i).drawScreen(app);
    }
    for (int i = 0; i < this.text.size(); i++) {
      text.get(i).drawScreen(app);
    }
  }

  public void checkClick(PApplet app) {
    for (int i = 0; i < buttons.size(); i++) {
      if (buttons.get(i).isHovering(app)){
      buttons.get(i).action.run();
      }
  }
}
}

class Text {
  int x, y, size;
  String text;
  boolean dynamic = false;
  Supplier<String> textSupplier;

  Text(String text, int x, int y, int size) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.text = text;
  }
  
  public void setTextSupplier(Supplier<String> s) {
    this.textSupplier = s;
    this.dynamic = true;
  }
  
   public void drawScreen(PApplet app) {
     app.textAlign(PConstants.CENTER, PConstants.CENTER);
     app.fill(Constants.TEXT_COLOR);
     app.textSize(this.size);
     if (dynamic) {
        app.text(this.textSupplier.get(), this.x, this.y);
     }
     else {
        app.text(this.text, this.x, this.y);
     }
   }
}

class Button {
  int x, y, w, h;
  String text;
  Runnable action;
  int textColor;

  Button(int x, int y, int w, int h, String text, Runnable action) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.text = text;
    this.action = action;
    
    this.textColor = Constants.TEXT_COLOR;
  }

  Button(int x, int y, int w, int h, int text, Runnable action) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.text = String.valueOf(text);
    this.action = action;
    
    this.textColor = Constants.TEXT_COLOR;
  }
  
  
  
  public void drawScreen(PApplet app) {
    app.rectMode(PConstants.CENTER);
    app.fill(Constants.mainColor);
    app.stroke(Constants.secondaryColor);
    app.strokeWeight(5);
    app.textAlign(PConstants.CENTER, PConstants.CENTER);
    app.textSize((this.h*3)/4);

    if (this.isHovering(app)) {
        app.rect((float)this.x, (float)this.y, (float)(this.w*1.1), (float)(this.h*1.1), ((float)this.w+(float)this.h)/10);
        app.textSize((float)(this.h*3.3)/4.f);
    } else {
        app.rect(this.x, this.y, this.w, this.h, (this.w+this.h)/10);
    }
    app.fill(textColor);
    app.text(this.text, this.x, this.y);
  }
  
  public void setTextColor(int rgb) {
    this.textColor = rgb;
  }

  public void checkClick(PApplet app) {
    if (isHovering(app)) {
      this.action.run();
    }
  }

  boolean isHovering(PApplet app) {
    return (app.mouseX < this.x+this.w/2 &&
      app.mouseX > this.x-this.w/2 &&
      app.mouseY < this.y+this.h/2 &&
      app.mouseY > this.y-this.h/2);
  }
}