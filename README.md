# body-simulator

è fornito il codice di un programma  che simula il movimento di N corpi su un piano bidimensionale, soggetti a urti 
elastici. Il programma è sequenziale, non strutturato.  
L’algoritmo che definisce il comportamento del simulatore - contenuta nella classe Simulator - in pseudocodice è il 
seguente:

```$xslt
vt = 0;     /* virtual time */         
dt  = 0.01; /* time increment at each iteration */
        
loop {
    Update bodies positions, given virtual time elapsed dt;
    Check and solve collisions;
    vt = vt + dt;   
    Display current stage;
}
```
Nel programma la class Body rappresenta una corpo e tiene traccia della sua posizione, velocità, aggiornate ad ogni 
frame, oltre che alle sue dimensioni (raggio).  

L’aggiornamento della posizione dei corpi avviene considerando un moto uniforme con urti elastici, senza ulteriori forze
che agiscano, in cui vale la conservazione della quantità di moto e dell’energia cinetica, assumendo corpi con la stessa
massa (rif: “Two-dimensional collision with two moving objects”, https://en.wikipedia.org/wiki/Elastic_collision).

La semplice GUI fornita nel programma semplicemente visualizza l’andamento della simulazione.  

## CONSEGNA

- Realizzare una versione concorrente della simulazione senza GUI, considerando un insieme iniziale N di corpi - e 
calcolando l’evoluzione temporale per un certo numero di passi Nsteps - con Nsteps fissato come parametro. Posizione e
velocità iniziali possono essere definite in modo casuale. L’obiettivo è:
  1. Massimizzare le performance, sfruttando tutte le capacità di calcolo del generico  sistema di elaborazione su cui è
   mandato in esecuzione il programma
  2. Organizzare il programma in modo modulare, estendibile.  
Analizzare le performance del programma considerando valori di N pari a 100, 1000, 5000 e raggio dei corpi pari a 0.01, 
con Nsteps pari a 500, 1000, 5000, calcolando lo speedup, e valutando il suo comportamento usando sia il numero ottimale
teorico di thread, sia considerando prove diverse con un numero variabile di threads per verificarne la scalabilità.   
 Usare JPF per verificare la correttezza del programma, considerandone la parte più significativa in merito, 
 opportunamente semplificata.

- Estendere la simulazione includendo una GUI con pulsanti start/stop per lanciare/ fermare la simulazione e 
visualizzare l’andamento, includendo informazioni circa il tempo virtuale. Analizzare la reattività del programma, il 
tempo massimo impiegato eseguire l’azione richiesta a fronte dell’input dell’utente.
Usare JPF per verificare la correttezza dell’estensione.

- FACOLTATIVO Estendere la simulazione includendo la modalità passo-passo - ovvero ad ogni frame visualizzato 
procedere al calcolo e visualizzazione del successivo mediante la pressione di un pulsante apposito.
