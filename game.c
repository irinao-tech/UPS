//
// Created by irina on 10.11.19.
//
//#include <stdio.h>
#include <stdlib.h>
#include "game.h"
#include "main.h"
#include <stdio.h>

void printMistnost(place *m){
    printf("Name = %s\n", m->name);
    printf("pocet hracu = %d\n", m->pocet_hracu);
    printf("Player1: \n");
    printf("\tName = %s\n", m->player1.name);
    printf("\troundScore = %d\n", m->player1.roundScore);
    printf("\tgameScore = %d\n", m->player1.gameScore);
    printf("\tsocket = %d\n", m->player1.socket);
    printf("Player2: \n");
    printf("\tName = %s\n", m->player2.name);
    printf("\troundScore = %d\n", m->player2.roundScore);
    printf("\tgameScore = %d\n", m->player2.gameScore);
    printf("\tsocket = %d\n", m->player2.socket);
}

void clearMistnost(place *m){
    m->pocet_hracu = 0;
    m->player1.gameScore = 0;
    m->player2.gameScore = 0;
}


int hod(){//int *cisla[6]){
    printf("Hazim kostkou\n");
    int score = 0;
    int cislo;
    //int cisla[6];
    for (int i = 0; i < 6; i++){
        cislo = (rand() % 6) + 1;
        printf("%d: %d\n",i+1, cislo);
        cisla[i] = cislo;
    }
    printf("\n");
    for( int i = 0; i < 3; i++){
        if(cisla[i] == 1 ){
            return score = 0;
        }
        score += cisla[i];
    }

    // spocitat vysledne body dle zadani
    return score;
}

void hra(place *m){ // pointer
    int size = 256;
    char sbuff[size];
    char buff[size];
    int reader;
    memset(sbuff, '\0', size);
    strcpy(sbuff, "Hra zacina.\t\n");
    broadcast(m->player1.socket, m->player2.socket, sbuff);
    //printf("Hra zacina...\n");
    int vyhra = 20;
    //int cisla[6];
    int vysledekHodu;
    int maxBodu = 0;
    int hraje = 1;
    char pokracovat;
    struct hrac vyherce;
    char *command; // sent command
    char *user; //user name
    str str;
    //int player = 1;
    while(1) {
        vysledekHodu = 0;
        for (int i = 0; i < m->pocet_hracu; i++) {
            hraje = 1;
            printf("Hraje hrac %d\n", i+1);
            memset(sbuff, '\0', size);
            strcpy(sbuff, "Hraje hrac ");
            sprintf(str, "%d", i+1);
            strcat(sbuff, str);
            strcat(sbuff, "\n");
            broadcast(m->player1.socket, m->player2.socket, sbuff);
            while(hraje) {
                //printf("Pred hodem kostkou.\n");
                vysledekHodu = hod();//(int **) &cisla);
                sleep(1);
                //broadcast results to both players
                for(int i =0; i<3; i++) {
                    memset(sbuff, '\0', size);
                    sprintf(str, "%d", cisla[i]);
                    strcat(sbuff, str);
                    strcat(sbuff, "\n");
                    broadcast(m->player1.socket, m->player2.socket, sbuff);
                    sleep(1);
                }

                printf("Vysledek hodu = %d\n", vysledekHodu);
                //printf("Pred send \n");
                memset(sbuff, '\0', size);
                strcpy(sbuff, "Vysledek hodu =  ");
                sprintf(str, "%d", vysledekHodu);
                strcat(sbuff, str);
                strcat(sbuff, "\n");
                broadcast(m->player1.socket, m->player2.socket, sbuff);
                memset(sbuff, '\0', size);

                if (vysledekHodu > 0) {
                    if (i == 0) { // hraje prvni hrac
                        m->player1.roundScore += vysledekHodu;
                        printf("Hraje hrac: %s\n", m->player1.name);
                        strcpy(sbuff, "pokracovat dalsim hodem? A-ano , N-ne :\n");
                        send(m->player1.socket, sbuff, sizeof(sbuff), 0);
                        printf("Po send 1. hracu\n");
                    } else { // hraje druhy hrac
                        m->player2.roundScore += vysledekHodu;
                        printf("Hraje hrac: %s\n", m->player2.name);
                        strcpy(sbuff, "pokracovat dalsim hodem? A-ano, N-ne :\n");
                        send(m->player2.socket, sbuff, sizeof(sbuff), 0);
                        printf("Po send 2. hracu \n");
                    }

                    printf("Pokracovat dalsim hodem? \n");
                    memset(buff, '\0', size);
                    //Receive message
                    if (i == 0) { //pokud hraje 1. hrac
                        printf("CONTROL RCV 1.hrac\n");
                        receive(m->player1.socket, buff);
                        command = strtok(buff, ":"); //split received message in name and command
                        user = command;
                        command = strtok(NULL, ":");
                        printf("\n%s sent: %s\n", user, command);
                        if (strncmp(" A", command, 2) == 0){
                            memset(sbuff, '\0', size);
                            strcpy(sbuff, "Protihrac pokracuje...\n");
                            send(m->player2.socket, sbuff, sizeof(sbuff), 0);
                        }
                        //m->player1.ans = command;
                        //printf("player1 ans = '%s'\n", m->player1.ans);
                    }else { //pokud hraje 2. hrac
                        printf("CONTROL RCV 2.hrac\n");
                        receive(m->player2.socket, buff);
                        printf("2CONTROL RCV 2. hrac \n");
                        command = strtok(buff, ":"); //split received message in name and command
                        user = command;
                        command = strtok(NULL, ":");
                        printf("\n%s sent: %s\n", user, command);
                        if (strncmp(" A", command, 2) == 0){
                            memset(sbuff, '\0', size);
                            strcpy(sbuff, "Protihrac pokracuje...\n");
                            send(m->player1.socket, sbuff, sizeof(sbuff), 0);
                        }
                    }

                    //Kontrola odpovedi na otazku jestli chce pokracovat
                    printf("Pred controlou rcv \n");
                    if (strncmp(" A", command, 2) == 0) {
                        hraje = 1;
                    } else { // hrac konci svoje kolo
                        hraje = 0;
                        if (i == 0) { // hraje prvni hrac
                            m->player1.gameScore += m->player1.roundScore;
                            m->player1.roundScore = 0;
                        } else { // hraje druhy hrac
                            m->player2.gameScore += m->player2.roundScore;
                            m->player2.roundScore = 0;
                        }
                    }
                } else {
                    hraje = 0;
                }
            }
            memset(sbuff, '\0', size);
            strcpy(sbuff, "Celkovy pocet hodu hrace 1 =  ");
            sprintf(str, "%d", m->player1.gameScore);
            strcat(sbuff, str);
            strcat(sbuff, "\n");
            strcat(sbuff, "Celkovy pocet hodu hrace 2 =  ");
            sprintf(str, "%d", m->player2.gameScore);
            strcat(sbuff, str);
            strcat(sbuff, "\n");
            broadcast(m->player1.socket, m->player2.socket, sbuff);
            //send(m->player1.socket, sbuff, sizeof(sbuff), 0);
            //send(m->player2.socket, sbuff, sizeof(sbuff), 0);
            memset(sbuff, '\0', size);
        }
        printf("control if smbd won--------------------------------------------------------------\n");
        if ((m->player1.gameScore >= vyhra) || (m->player2.gameScore >= vyhra)) { // nektery hrac jiz vyhral // TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            printf("!!!!!!!!!!!!!!!!!smbd won!!!!!!!!!!!!!!!!!!!\n");
            if (m->player1.gameScore == m->player2.gameScore) { // remiza
                printf("Remiza: pocet bodu = %d", m->player1.gameScore);
                memset(sbuff, '\0', size);
                strcpy(sbuff, "Vysledek hry: Remiza. Pocet bodu=  ");
                sprintf(str, "%d", m->player1.gameScore);
                strcat(sbuff, str);
                strcat(sbuff, "\n");
                broadcast(m->player1.socket, m->player2.socket, sbuff);
                //send(m->player1.socket, sbuff, sizeof(sbuff), 0);
                //send(m->player2.socket, sbuff, sizeof(sbuff), 0);
            } else if (m->player1.gameScore > m->player2.gameScore) { // vyhral prvni hrac
                printf("Vyhral hrac %s a ma %d bodu.", m->player1.name, m->player1.gameScore);
                memset(sbuff, '\0', size);
                strcpy(sbuff, "Vyhral hrac ");
                strcat(sbuff, m->player1.name );
                strcat(sbuff, ". Pocet bodu: " );
                sprintf(str, "%d", m->player1.gameScore);
                strcat(sbuff, str);
                strcat(sbuff, "\n");
                broadcast(m->player1.socket, m->player2.socket, sbuff);
                //send(m->player1.socket, sbuff, sizeof(sbuff), 0);
                //send(m->player2.socket, sbuff, sizeof(sbuff), 0);
            } else { // vyhral druhy hrac
                printf("Vyhral hrac %s a ma %d bodu.", m->player2.name, m->player2.gameScore);
                memset(sbuff, '\0', size);
                strcpy(sbuff, "Vyhral hrac ");
                strcat(sbuff, m->player2.name );
                strcat(sbuff, ". Pocet bodu: " );
                sprintf(str, "%d", m->player2.gameScore);
                strcat(sbuff, str);
                strcat(sbuff, "\n");
                //send(m->player1.socket, sbuff, sizeof(sbuff), 0);
                //send(m->player2.socket, sbuff, sizeof(sbuff), 0);
                broadcast(m->player1.socket, m->player2.socket, sbuff);
            }
            return;
        }
    }
    exit(0);
}