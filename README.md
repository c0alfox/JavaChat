# JavaChat

Chat con interfaccia grafica in Java 

## Protocollo di connessione

Il protocollo si articola con messaggi testuali codificati in standard utf-8.

Ogni messaggio è strutturato come segue: `<carattere iniziale> <corpo del messaggio>`

Il carattere iniziale è codificato e può assumere solo i seguenti valori:

- `c` (da command) - Il client invia al server `c <stringa comando>` e il server risponderà secondo le specifiche alla sezione [comandi](#comandi);
- `m` (da message) - Se il client invia al server `m <username> <password>`, il messaggio verrà inviato a chiunque è connesso allo stesso canale dell'utente. Se il server invia un pacchetto formato analogamente, allora il client dovrà interpretarlo come messaggio in arrivo;
- `s` (da suggestion) - TBD;
- `j` (da join) - Il server invia `j <username> <colore>` ad ogni client collegato ad un canale dove si è aggiunto un nuovo membro, dove username è il nome utente e color è un codice di 6 cifre esadecimali che rappresenta il colore associato all'utente;
- `l` (da leave) - Il server invia `l <username>` ad ogni client collegato ad un canale dove un membro ha appena abbandonato;
- `u` (da update) - Il server invia `u <username> <colore>` ad ogni client collegato ad un canale dove un utente ha deciso di modificare il suo colore associato;
- `d` (da data) - Il server invia `d <n> ` seguito da $n$ coppie di `<username> <colore>` intervallate da spazi singoli, a indicare gli utenti presenti in un canale;
- `e` (da error) - Il server invia `e <stringa di errore>` in caso di un qualunque errore.

## Comandi

I comandi sono interpretati in maniera case insensitive e cominciano tutti con il carattere `/`. Il server riceverà solo ed esclusivamente la stringa di comando, ovvero la parte successiva al carattere `/`.

Sono comandi validi:
- `/join <nome canale>`, serve a entrare in un canale o a cambiare il canale attuale. L'utente può chattare su un canale per volta. `<nome canale>` deve essere una stringa senza spazi, altrimenti il server restituisce un errore.
- `/channels`, mostra la lista dei canali aperti;
- `/mychannel`, ritorna il nome del canale attuale;
- `/users`, ritorna la lista di utenti collegati al canale attuale.