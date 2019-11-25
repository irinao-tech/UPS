//
// Created by irina on 13.11.19.
//

#ifndef SERVER_MAIN_H
#define SERVER_MAIN_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>

#include "structures.h"
#define PORT 10157
#define LOBBYSIZE 3

place lobby[LOBBYSIZE];

void initializeLobby();
void sendMess(int newSocket, char *sbuff, int lenBuff);
typedef char *str[3];
void receive(int socket, char *buff);
char parseRecvMess(char *buff);
void broadcast(int socket, int socket1, char *sbuff);
void addToLobby(player *hrac);
int main();

#endif //SERVER_MAIN_H
