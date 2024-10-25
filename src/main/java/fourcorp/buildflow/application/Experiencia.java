package fourcorp.buildflow.application;//package fourcorp.buildflow.application;
import fourcorp.buildflow.domain.*;
import fourcorp.buildflow.repository.Clock;
import fourcorp.buildflow.repository.ProductPriorityLine;
import fourcorp.buildflow.repository.Repositories;
import fourcorp.buildflow.repository.WorkstationsPerOperation;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Experiencia {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Workstation> workstations = new ArrayList<>();
    private static final Clock clock = new Clock();



    public static void main(String[] args) {
        // Criando algumas estações de trabalho para testes
        workstations.add(new Workstation("W1", 5)); // 5 segundos
        workstations.add(new Workstation("W2", 10)); // 10 segundos

        int choice;
        do {
            System.out.println("\nMenu de Teste:");
            System.out.println("1. Iniciar operação na Workstation 1");
            System.out.println("2. Iniciar operação na Workstation 2");
            System.out.println("3. Parar contagem de espera de uma Workstation");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    startOperation(0); // Workstation 1
                    break;
                case 2:
                    startOperation(1); // Workstation 2
                    break;
                case 3:
                    stopOperation(); // Parar contagem de espera
                    break;
                case 4:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (choice != 4);

        scanner.close();
    }

    private static void startOperation(int index) {
        if (index < 0 || index >= workstations.size()) {
            System.out.println("Workstation inválida!");
            return;
        }

        Workstation workstation = workstations.get(index);
        workstation.startClock(index, true);
    }

    private static void stopOperation() {
        System.out.print("Escolha a Workstation para parar a contagem de espera (1 ou 2): ");
        int workstationIndex = scanner.nextInt() - 1;

        if (workstationIndex < 0 || workstationIndex >= workstations.size()) {
            System.out.println("Workstation inválida!");
            return;
        }

        Workstation workstation = workstations.get(workstationIndex);
        int totalWaitingTime = workstation.stopClock(); // Parar contagem e pegar o tempo total
        System.out.println("Contagem de espera parada. Tempo total: " + totalWaitingTime + " segundos.");
    }
}

