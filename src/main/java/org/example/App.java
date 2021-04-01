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

            //Define the frequencies of interest
            float freqMin = 10000;
            float freqMax = 16000;

            //Loop through the fft bins and filter frequencies
            for(int fftBin = 0; fftBin < FFT_SIZE; fftBin++){
                //Calculate the frequency of this bin assuming a sampling rate of 44,100 Hz
                float frequency = (float)fftBin * 88200F / (float)FFT_SIZE;

                //Now filter the audio, I'm assuming you wanted to keep the
                //frequencies of interest rather than discard them.
                if(frequency  < freqMin || frequency > freqMax){
                    //Calculate the index where the real and imaginary parts are stored
                    int real = 2 * fftBin;
                    int imaginary = 2 * fftBin + 1;

                    //zero out this frequency
                    buffer[real] = 0;
                    buffer[imaginary] = 0;
                }
            }

            fft.realInverse(buffer, false);
            WavFile outFile = WavFile.newWavFile(new File("/home/meti/Music/Pink Noise/out.wav"), 1, wavFile.getNumFrames(), 16, wavFile.getSampleRate());
            outFile.writeFrames(buffer, numberOfFrames);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WavFileException e) {
            e.printStackTrace();
        }
    }
}
