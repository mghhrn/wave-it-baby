package org.example;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main( String[] args ) {
        try {
            WavFile wavFile = WavFile.openWavFile(new File("/home/meti/Music/Pink Noise/pink_88k_-3dBFS.wav"));
            wavFile.display();

            int numberOfFrames = (int)wavFile.getNumFrames();
            double[] buffer = new double[numberOfFrames];
            wavFile.readFrames(buffer, numberOfFrames);

            long FFT_SIZE = wavFile.getNumFrames() / 2;
            DoubleFFT_1D fft = new DoubleFFT_1D(FFT_SIZE);
            fft.realForward(buffer);

            System.out.println("After read forward full:");
            for (int i = 0 ; i < 30 ; i++) {
                System.out.println(buffer[i]);
            }

            //Define the frequencies of interest
            float freqMin = 5000;
            float freqMax = 6000;

            //Loop through the fft bins and filter frequencies
            for(int fftBin = 0; fftBin < FFT_SIZE; fftBin++){
                //Calculate the frequency of this bin based on the sampling rate of wave file
                float frequency = (float)fftBin * 88200F / (float)FFT_SIZE;

                //Now filter the audio
                if( freqMin < frequency && frequency < freqMax){
                    //Calculate the index where the real and imaginary parts are stored
                    int real = 2 * fftBin;
                    int imaginary = 2 * fftBin + 1;

                    //zero out this frequency
                    buffer[real] = 0;
                    buffer[imaginary] = 0;
                }
            }

            long startTime = System.currentTimeMillis();
            fft.realInverse(buffer, true);
            long endTime = System.currentTimeMillis();
            System.out.printf("The duration for real inverse is %d seconds!\n", (endTime - startTime)/1000);
            WavFile outFile = WavFile.newWavFile(new File("/home/meti/Music/out_realFroward_realInverse_scaled_filtered.wav"), 1, wavFile.getNumFrames()/2, 16, wavFile.getSampleRate());
            outFile.writeFrames(buffer, numberOfFrames/2);
            outFile.close();
            System.out.println("out file closed!");
            wavFile.close();
            System.out.println("wav file closed!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WavFileException e) {
            e.printStackTrace();
        }
    }
}
