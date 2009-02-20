import processing.core.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; public class subjects extends PApplet {/*
    subjects - Art computing piece.
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

int min_ghosts = 4;
int max_ghosts = 24;

// In pixels

int canvas_width = 400;
int canvas_height = 400;

int min_ghost_x = -100;
int max_ghost_x = 100;
int min_ghost_y = -100;
int max_ghost_y = 100;

float min_ghost_start_t = 0.0f;
float max_ghost_start_t = 0.5f;

int min_ghost_size = 5;
int max_ghost_size = 200;

// 0..255

int min_ghost_shade = 10;
int max_ghost_shade = 80;

// In seconds

float min_duration = 1.0f;
float max_duration = 10.0f;

// The ghost population

int num_ghosts;
float[] xs;
float[] ys;
float[] ts;
float[] t_scale_factors;
float[] sizes;
int[] shades;
float rotation;

// The current ghost population timeline

float start_growing;
float stop_growing;
float start_shrinking;
float stop_shrinking;

float grow_factor;
float shrink_factor;

// Make the ghosts

public void gen_ghosts ()
{
  rotation = random (PI / 2.0f);
  
  num_ghosts = (int)random(min_ghosts, max_ghosts);
  
  xs = new float[num_ghosts]; 
  ys = new float[num_ghosts]; 
  ts = new float[num_ghosts];
  t_scale_factors = new float[num_ghosts];
  sizes = new float[num_ghosts]; 
  shades = new int[num_ghosts]; 
  
  for (int i = 0; i < num_ghosts; i++) 
  {
   xs[i] = random (min_ghost_x, max_ghost_x); 
   ys[i] = random (min_ghost_y, max_ghost_y); 
   ts[i] = random (min_ghost_start_t, max_ghost_start_t);
   t_scale_factors[i] = 1.0f / (1.0f - ts[i]);
   sizes[i] = random (min_ghost_size, max_ghost_size); 
   shades[i] = color(100, 100, 100, 10); 
  }
}

public float random_duration ()
{
 return random (min_duration, max_duration) * 1000; 
}

// make the timeline for the ghosts

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
  
public void draw_ghost (int which, float scale_factor)
{
  if (scale_factor < ts[which])
  {
    return; 
  }
  float scale_scaled = (scale_factor - ts[which]) * t_scale_factors[which];
  println (scale_scaled);
  float side_length = sizes[which] * scale_scaled;
  if (side_length > sizes[which])
  {
   side_length = sizes[which]; 
  }
  fill (shades[which]);
  rect (xs[which] * scale_scaled, ys[which] * scale_scaled, side_length, side_length);
}

public void draw_ghosts ()
{
  float scale_factor = scale_factor (millis ());
  if (scale_factor == 0)
  {
   gen_ghosts ();
   gen_timeline ();
  }
  for (int i = 0; i < num_ghosts; i++)
  {
   draw_ghost (i, scale_factor);
  } 
}

public void setup ()
{
  size(canvas_width, canvas_height); 
  frameRate(30);
  gen_ghosts ();
  gen_timeline ();
}

public void draw ()
{
  background(255);
  translate (canvas_width / 2.0f, canvas_height / 2.0f);
  noStroke ();
  smooth ();
  rectMode (CENTER);
  draw_ghosts (); 
}
static public void main(String args[]) {   PApplet.main(new String[] { "subjects" });}}