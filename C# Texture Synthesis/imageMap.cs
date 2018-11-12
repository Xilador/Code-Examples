using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing.Imaging;
using System.Drawing;

class imageMap {
    public int[,,] map;
    public int width, height;
    Bitmap preview;

    //create a regular image map
    public imageMap(Image img) {
        this.width = img.Width;
        this.height = img.Height;
        this.preview = new Bitmap(img);
        //generate map from Bitmap
        generateMap();
    }

    //Create a white noise image map
    public imageMap(int x, int y) {
        this.width = x;
        this.height = y;
        //generate map with white noise
        generateWhiteNoise(x, y);
        //generate Image
        generateImage();
    }

    public Image getPicture() {
        return (Image)preview;
    }

    public void setPixel(int x, int y, int r, int g, int b) {
        preview.SetPixel(x, y, Color.FromArgb(255,r,g,b));
        this.map[x, y, 0] = r;
        this.map[x, y, 1] = g;
        this.map[x, y, 2] = b;
    } 

    void generateImage() {
        this.preview = new Bitmap(width, height);
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.preview.SetPixel(x, y, Color.FromArgb(255, this.map[x, y, 0], this.map[x, y, 1], this.map[x, y, 2]));
            }
        }
    } 

    void generateWhiteNoise(int width, int height) {
        this.map = new int[width, height, 3];
        Random rng = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < 3; z++) {
                    this.map[x, y, z] = rng.Next(256);
                }
            }
        }
    }

    void generateMap() {
        int width = preview.Width, height = preview.Height;
        Color px;
        this.map = new int[ width, height, 3];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                px = preview.GetPixel(x, y);
                map[x, y, 0] = px.R;
                map[x, y, 1] = px.G;
                map[x, y, 2] = px.B;
            } 
        } 

    }
    
}
