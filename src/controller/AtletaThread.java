package controller;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class AtletaThread extends Thread {
    private static final int DISTANCIA_CORRIDA = 3000;
    private static final int DISTANCIA_CICLISMO = 5000;
    private static final int MAX_ARMAS = 5;
    private static final Semaphore armasDisponiveis = new Semaphore(MAX_ARMAS);

    private static final AtomicInteger rankingDistribuido = new AtomicInteger(250);
    private static final AtomicInteger proximoRanking = new AtomicInteger(0);

    private final int id;
    private int tempoCorrida;
    private int pontuacaoTiros;
    private int tempoTiro;
    private int tempoCiclismo;
    private int pontuacaoTotal;

    private static final int[] pontuacoes = new int[25];
    private static final int[] ids = new int[25];

    public AtletaThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            correr();
            armasDisponiveis.acquire();
            atirar();
            armasDisponiveis.release();
            pedalar();
            calcularPontuacao();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void correr() throws InterruptedException {
        int velocidadeCorrida = 20 + new Random().nextInt(6);
        tempoCorrida = DISTANCIA_CORRIDA / velocidadeCorrida * 30;
        System.out.println("Atleta " + id + " está correndo. Tempo estimado: " + tempoCorrida + " ms");
        Thread.sleep(tempoCorrida);
    }

    private void atirar() throws InterruptedException {
        System.out.println("Atleta " + id + " está atirando.");
        pontuacaoTiros = 0;
        for (int i = 1; i <= 3; i++) {
            tempoTiro = 500 + new Random().nextInt(2501);
            int pontuacao = new Random().nextInt(11);
            pontuacaoTiros += pontuacao;
            System.out.println("Atleta " + id + " fez o tiro " + i + " e marcou " + pontuacao + " pontos.");
            Thread.sleep(tempoTiro);
        }
        System.out.println("Atleta " + id + " terminou os tiros.");
    }

    private void pedalar() throws InterruptedException {
        int velocidadeCiclismo = 30 + new Random().nextInt(11);
        tempoCiclismo = DISTANCIA_CICLISMO / velocidadeCiclismo * 40;
        System.out.println("Atleta " + id + " está pedalando. Tempo estimado: " + tempoCiclismo + " ms");
        Thread.sleep(tempoCiclismo);
    }

    private void calcularPontuacao() {
        int pontuacaoCorrida = rankingDistribuido.getAndAdd(-10);
        pontuacaoTotal = pontuacaoCorrida + pontuacaoTiros;
        System.out.println("Atleta " + id + " terminou a prova com " + pontuacaoTotal + " pontos.");

        int posicao = proximoRanking.getAndIncrement();
        pontuacoes[posicao] = pontuacaoTotal;
        ids[posicao] = id;
    }

    public static void exibirResultadoFinal() {
        System.out.println("\nResultado Final:");
        for (int i = 0; i < pontuacoes.length - 1; i++) {
            for (int j = 0; j < pontuacoes.length - i - 1; j++) {
                if (pontuacoes[j] < pontuacoes[j + 1]) {
                    int tempPontuacao = pontuacoes[j];
                    pontuacoes[j] = pontuacoes[j + 1];
                    pontuacoes[j + 1] = tempPontuacao;
                    int tempId = ids[j];
                    ids[j] = ids[j + 1];
                    ids[j + 1] = tempId;
                }
            }
        }

        for (int i = 0; i < 25; i++) {
            System.out.println("Atleta " + ids[i] + " - Pontuação: " + pontuacoes[i]);
        }
    }
}