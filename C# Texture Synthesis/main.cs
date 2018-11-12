using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Windows.Forms;
using System.IO;
using System.Drawing.Imaging;

class main {

    static string source, synth;

    [STAThread]
    static void Main(string[] args) {
        bool stillRun = true, useNew = true;
        int width, height, accuracy;
        Image synthImage;
        synthImage i;
        string answer, answer2;
        while (stillRun) {
            Console.WriteLine("Welcome to the Texture Synthesis program");
            Console.WriteLine("Please choose a file to synthesize");
            chooseFile();
            Console.WriteLine("Now choose a save location");
            saveFile();
            Console.Write("Please choose the width of the image: ");
            width = Convert.ToInt32(Console.ReadLine());
            Console.Write("Please choose the height of the image: ");
            height = Convert.ToInt32(Console.ReadLine());
            useNew = true;
            while (useNew) {
                Console.Write("Please choose the level of accuracy(3 - 9): ");
                accuracy = Convert.ToInt32(Console.ReadLine());
                synth = Path.GetDirectoryName(source)+ "\\" + Path.GetFileNameWithoutExtension(source) + "_Synth_nhdb" + accuracy + ".jpg";
                Console.WriteLine("Now creating new blank image...");
                i = new synthImage(Image.FromFile(source), width, height, accuracy);
                Console.WriteLine("Now synthesizing the image....");
                synthImage = i.generateSyntheticTexture();
                synthImage.Save(synth, ImageFormat.Jpeg);
                Console.WriteLine("Done!");
                showImages();
                answer = "";
                answer2 = "";
                while (!answer.Equals("continue") && !answer.Equals("quit"))
                {
                    Console.Write("Do you wish to <continue> or <quit>");
                    answer = (Console.ReadLine()).ToLower();
                }
                if (answer.Equals("quit")) {
                    stillRun = false;
                    useNew = false;
                }
                else
                {
                    while (!answer2.Equals("new") && !answer2.Equals("same"))
                    {
                        Console.Write("Do you wish to use a <new> image or the <same> one?");
                        answer2 = (Console.ReadLine()).ToLower();
                    }
                }
                if (answer.Equals("new")) useNew = false;
            } 
           
        } 
    }

    [STAThread]
    static void chooseFile() {
        OpenFileDialog openFileDialog1 = new OpenFileDialog();

        openFileDialog1.InitialDirectory = "c:\\";
        openFileDialog1.Filter = 
            "Image files (*.jpg, *.jpeg, *.jpe, *.jfif, *.png) | *.jpg; *.jpeg; *.jpe; *.jfif; *.png";
        openFileDialog1.FilterIndex = 0;
        openFileDialog1.RestoreDirectory = true;

        if (openFileDialog1.ShowDialog() == DialogResult.OK)
        {
            source = openFileDialog1.FileName;
        }
    }

    [STAThread]
    static void saveFile() {

        FolderBrowserDialog fbd = new FolderBrowserDialog();
        DialogResult result = fbd.ShowDialog();

        if (result == DialogResult.OK && !string.IsNullOrWhiteSpace(fbd.SelectedPath))
        {
            synth = fbd.SelectedPath;
        }
    } 

    static void showImages() {
        PictureBox pb0 = new PictureBox();
        pb0.ImageLocation = source;
        pb0.SizeMode = PictureBoxSizeMode.AutoSize;

        PictureBox pb1 = new PictureBox();
        pb1.ImageLocation = synth;
        pb1.SizeMode = PictureBoxSizeMode.AutoSize;
    }

}
