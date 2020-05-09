
package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DS_IP92_LR72_MedvedievM {

    public static void main(String[] args) throws IOException {
        DicotyledonousGraph graph = new DicotyledonousGraph(new File("inputs/input2.txt"));
        graph.printPairs();
//        System.out.println(Graph.matrixToString(graph.adjacencyMatrix, ""));
//        graph.printPairs();
    }

}

abstract class Graph {
    protected int[][] verges;
    protected int numberOfNodes, numberOfVerges;// n вершин, m ребер
    protected int[][] incidenceMatrix, adjacencyMatrix;
    protected boolean isDicotyledonous = true;
    protected ArrayList<Integer> leftNodes = new ArrayList<>(), rightNodes = new ArrayList<>();

    protected Graph(File file) throws FileNotFoundException {
        parseFile(file);
        preSetAdjacencyMatrix();
    }

    private void parseFile(File file) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(file);
        this.numberOfNodes = fileScanner.nextInt();
        this.numberOfVerges = fileScanner.nextInt();
        this.verges = new int[this.numberOfVerges][2];
        for (int i = 0; i < this.numberOfVerges; i++) {
            verges[i][0] = fileScanner.nextInt();
            verges[i][1] = fileScanner.nextInt();
            if(rightNodes.contains(verges[i][0]) || leftNodes.contains(verges[i][1]))
            {
                int temp = verges[i][0];
                verges[i][0] = verges[i][1];
                verges[i][1] = temp;
            }

            if(rightNodes.contains(verges[i][0]) || leftNodes.contains(verges[i][1]))
                isDicotyledonous = false;

            if (!leftNodes.contains(verges[i][0]))
                leftNodes.add(verges[i][0]);

            if (!rightNodes.contains(verges[i][1]))
                rightNodes.add(verges[i][1]);


        }
    }



    protected void preSetAdjacencyMatrix() {
        this.adjacencyMatrix = new int[this.numberOfNodes][this.numberOfNodes];
    }



    protected static String matrixToString(int[][] matrix, String extraText) {
        StringBuilder outputText = new StringBuilder(extraText + "\n");

        for (int[] line : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                String text = (line[j] != -1) ? Integer.toString(line[j]) : "*";
                outputText.append(text).append(" ");

            }
            outputText.append("\n");
        }
        return outputText.toString();
    }

}

class DicotyledonousGraph extends Graph {
    protected DicotyledonousGraph(File file) throws FileNotFoundException {
        super(file);
//        System.out.println(getTypeOfConnectedness());
//        System.out.println(matrixToString(this.adjacencyMatrix,""));
    }

    protected void preSetAdjacencyMatrix() {
        this.adjacencyMatrix = new int[leftNodes.size()][rightNodes.size()];
        for (int i = 0; i < this.numberOfVerges; i++)
            this.adjacencyMatrix[leftNodes.indexOf(verges[i][0])][rightNodes.indexOf(verges[i][1])] = 1;
    }

    public void printPairs() {
        if(!isDicotyledonous)
        {
            System.out.println("Graph is not dicotyledonous!");
            return;
        }
        System.out.println("Possible Matrixs with perfect pairs: ");
        int[][] newAdjacencyMatrix = getCopyOfMatrix(this.adjacencyMatrix);
        for (int i = 0; i < newAdjacencyMatrix.length; i++)
            for (int j = 0; j < newAdjacencyMatrix[0].length; j++)
                newAdjacencyMatrix[i][j]--;
//        System.out.println(matrixToString(newAdjacencyMatrix, ""));
        printPairsRecurs(newAdjacencyMatrix, 0);
    }

    private void printPairsRecurs(int[][] adjMatrix, int level) {
        if (level >= adjMatrix.length) {
            printAdjacencyMatrix(adjMatrix);
            return;
        }
        boolean flag = false;
        for (int i = 0; i < adjMatrix[0].length; i++) {
            if (adjMatrix[level][i] != -1) {
                boolean hasOnes = false;
                for (int j = 0; j < level; j++) {
                    if (adjMatrix[j][i] == 1) {
                        hasOnes = true;
                        break;
                    }
                }

                if (!hasOnes) {
                    flag = true;
                    int[][] newAdjMatrix = getCopyOfMatrix(adjMatrix);
                    newAdjMatrix[level][i] = 1;
//                    System.out.println(matrixToString(adjMatrix, "Adj"));
//                    System.out.println(matrixToString(newAdjMatrix, "New Adj"));
                    printPairsRecurs(newAdjMatrix, level + 1);
                }
            }
        }
    }

    private int[][] getCopyOfMatrix(int[][] matrix) {
        int[][] output = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, output[i], 0, matrix.length);
        }
        return output;
    }

    private void printAdjacencyMatrix(int [][]adjMatrix) {
//        System.out.println(Arrays.toString(rightNodes.toArray()));
        StringBuilder text = new StringBuilder("  ");
        for (Integer rightNode : rightNodes) {
            text.append(rightNode).append(" ");
        }
        text.append("\n");
        for(int i=0;i<leftNodes.size();i++){
            text.append(leftNodes.get(i)).append(" ");
            for(int j=0;j<rightNodes.size();j++){
                text.append((adjMatrix[i][j] != -1) ? adjMatrix[i][j] : "*").append(" ");
            }
            text.append("\n");
        }
        System.out.println(text);
    }
}
