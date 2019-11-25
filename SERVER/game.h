//
// Created by irina on 10.11.19.
//

#ifndef SERVER_GAME_H
#define SERVER_GAME_H


#include "structures.h"
#include "main.h"

place gameArray[LOBBYSIZE];
int cisla[3]; // array for saving results of hod()

void clearMistnost(place *m);
void printMistnost(place *m);
int hod();
void hra(place *m);


#endif //SERVER_GAME_H

