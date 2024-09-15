package view;

import controller.AtletaThread;

public class Principal {
    public static void main(String[] args) {
        Thread[] atletas = new Thread[25];

        for (int i = 0; i < 25; i++) {
            atletas[i] = new AtletaThread(i + 1);
            atletas[i].start();
        }

        for (int i = 0; i < 25; i++) {
            try {
                atletas[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AtletaThread.exibirResultadoFinal();
    }
}
