import processing.core.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; public class congress extends PApplet {/*
    congress - Art computing piece.
    Copyright (C) 2007 Rob Myers rob@robmeyrs.org

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/    


// Configuration constants

int min_objects = 4;
int max_objects = 24;

// In pixels

int canvas_width = 400;
int canvas_height = 400;

int min_object_x = -100;
int max_object_x = 100;
int min_object_y = -100;
int max_object_y = 100;

float min_object_start_t = 0.0f;
float max_object_start_t = 0.5f;

int min_object_size = 5;
int max_object_size = 200;

// 0..255

int min_object_shade = 10;
int max_object_shade = 80;

// In seconds

float min_duration = 1.0f;
float max_duration = 10.0f;

// The object population

int num_objects;
float[] xs;
float[] ys;
float[] zs;
float[] sizes;
float[] ts;
float[] t_scale_factors;
int[] shades;
float rotation;

// The current object population timeline

float start_growing;
float stop_growing;
float start_shrinking;
float stop_shrinking;

float grow_factor;
float shrink_factor;

// Make the objects

public void gen_objects ()
{
  rotation = random (PI / 2.0f);
  
  num_objects = (int)random(min_objects, max_objects);
  
  xs = new float[num_objects]; 
  ys = new float[num_objects]; 
  zs = new float[num_objects]; 
  ts = new float[num_objects];
  t_scale_factors = new float[num_objects];
  sizes = new float[num_objects]; 
  shades = new int[num_objects]; 
  
  for (int i = 0; i < num_objects; i++) 
  {
   xs[i] = random (min_object_x, max_object_x); 
   ys[i] = random (min_object_y, max_object_y); 
   zs[i] = 0.0f;//random (min_object_y, max_object_y); 
   ts[i] = random (min_object_start_t, max_object_start_t);
   t_scale_factors[i] = 1.0f / (1.0f - ts[i]);
   sizes[i] = random (min_object_size, max_object_size); 
   shades[i] = color(100, 100, 100, 10);  
  }
}

public float random_duration ()
{
 return random (min_duration, max_duration) * 1000; 
}

// make the timeline for the objects

public void gen_timeline ()
{
  start_growing = millis ();
  stop_growing = start_growing + random_duration ();
  start_shrinking = stop_growing + random_duration ();
  stop_shrinking = start_shrinking + random_duration ();
  
  grow_factor = 1.0f / (stop_growing - start_growing);
  shrink_factor = 1.0f / (stop_shrinking - start_shrinking);
}

public float scale_factor (float t)
{
 if (t > stop_shrinking)
 {
   return 0;
 } 
 else if (t > start_shrinking)
 {
   return 1.0f - ((t - start_shrinking) * shrink_factor);
 }
 else if (t > stop_growing)
 {
   return 1.0f;
 }
 else if (t > start_growing)
 {
  return (t - start_growing) * grow_factor; 
 }
  // So <= start_growing
  return  0.0f;
}
  
public void draw_object (int which, float scale_factor)
{
  if (scale_factor < ts[which])
  {
    return; 
  }
  float scale_scaled = (scale_factor - ts[which]) * t_scale_factors[which];
  float side_length = sizes[which] * scale_scaled;
  if (side_length > sizes[which])
  {
   side_length = sizes[which]; 
  }
  pushMatrix ();
  translate (xs[which] * scale_scaled, ys[which] * scale_scaled, zs[which] * scale_scaled);
  rotateX (- PI / 8.0f);
  rotateY (PI / 8.0f);
  box (side_length);
  popMatrix ();
}

public void draw_objects ()
{
  float now = millis ();
  float scale_factor = scale_factor (now);
  if (scale_factor == 0)
  {
   gen_objects ();
   gen_timeline ();
  }
  for (int i = 0; i < num_objects; i++)
  {
   draw_object (i, scale_factor);
  } 
}

public void setup ()
{
  size(canvas_width, canvas_height, P3D); 
  frameRate(30);
  gen_objects ();
  gen_timeline ();
}

public void draw ()
{
  background(255);
  // fill (255, 180, 175, 200);
  // fill (250, 160, 145, 200);
  //fill (255, 201, 188, 200);
  fill (250, 230, 210, 255);
  ambientLight (245, 245, 245);
  directionalLight (20, 20, 20, 0, 1, -1);
  translate (canvas_width / 2.0f, canvas_height / 2.0f,
             - (max (canvas_width, canvas_height) * 0.4f));
  noStroke ();
  draw_objects ();   
}
static public void main(String args[]) {   PApplet.main(new String[] { "congress" });}}