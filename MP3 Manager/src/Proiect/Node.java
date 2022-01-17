package Proiect;

import java.io.File;

class Node {
    int data;
    File locatie;
    String nume_melodie;
    Node parent;
    Node left;
    Node right;
    int color; // 1 . Red, 0 . Black
}