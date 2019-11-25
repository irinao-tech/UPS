//
// Created by irina on 10.11.19.
//

#ifndef SERVER_STRUCTURES_H
#define SERVER_STRUCTURES_H

struct mistnost;

typedef struct hrac {
    char *name;
    int socket;
    int id;
    int gameScore;
    int roundScore;
    struct hrac *next;
    struct mistnost *mist;
} player;

typedef struct mistnost {
    char *name;
    int id;
    int pocet_hracu;
    int max_pocet_hracu;
    struct hrac player1;
    struct hrac player2;
    struct mistnost *next;
} place;



#endif //SERVER_STRUCTURES_H
