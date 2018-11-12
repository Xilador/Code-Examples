using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;


class synthImage {
    imageMap source;
    imageMap synth;
    int nhbdSize;

    public synthImage(Image source, int width, int height, int nhbd) {
        this.nhbdSize = nhbd;
        this.source = new imageMap(source);
        this.synth = new imageMap(width, height);
    }

    int modulus(int x, int m) {
        return (x % m + m) % m;
    } 

    public Image generateSyntheticTexture() {
        int srcHeight = source.height, srcWidth = source.width;
        int synHeight = synth.height, synWidth = synth.width;
        int radius = nhbdSize / 2;
        int srx, sry, syx, syy;
        int bestScore, bestX = 0, bestY = 0;
        int nhbdScore;

        for (int synY = 0; synY < synHeight; synY++) {
            for (int synX = 0; synX < synWidth; synX++) {
                bestScore = 999999999;
                for (int srcY = 0; srcY < srcHeight; srcY++) {
                    for (int srcX = 0; srcX < srcWidth; srcX++) {
                        nhbdScore = 0;
                        for (int ny = -radius; ny <= 0; ny++) {
                            for (int nx = -radius; nx <= radius; nx++) {
                                if (nx == 1 && ny == 0) goto nhbdLoop;
                                srx = modulus((srcX + nx) , (srcWidth));
                                sry = modulus((srcY + ny) , (srcHeight));
                                syx = modulus((synX + nx) , (synWidth));
                                syy = modulus((synY + ny) , (synHeight));
                                nhbdScore += (Math.Abs(synth.map[syx, syy, 0] - source.map[srx, sry, 0]))
                                    + (Math.Abs(synth.map[syx, syy, 0] - source.map[srx, sry, 0]))
                                    + (Math.Abs(synth.map[syx, syy, 0] - source.map[srx, sry, 0]));
                            }
                        }
                        nhbdLoop:;
                        if (nhbdScore < bestScore) {
                            bestScore = nhbdScore;
                            bestX = srcX;
                            bestY = srcY;
                        } 
                    }
                }
                synth.setPixel(synX, synY, source.map[bestX, bestY, 0], 
                    source.map[bestX, bestY, 1], source.map[bestX, bestY, 2]);
            } 
        }
        return synth.getPicture();
    }

}
