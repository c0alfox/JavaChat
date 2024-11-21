# JavaChat

Chat con interfaccia grafica in Java 

## Protocollo di connessione

Il protocollo si articola con messaggi testuali codificati in standard utf-8.

Ogni messaggio è strutturato come segue:
`<carattere iniziale> <corpo del messaggio>`

Il carattere iniziale è codificato e può assumere solo i seguenti valori:
`c` (da command) - In caso il carattere iniziale assuma questo valore, il corpo del messaggio contiene la stringa inserita dall'utente come comando;
`m` (da message) - In caso il carattere iniziale assuma questo valore, il corpo del messaggio è formattato come segue: `<username> <messaggio>`. L'username ed il messaggio sono considerati campi a sé stanti per permettere al client di colorare il nome utente secondo i criteri inviati dal server;
`s` (da suggestion) - TBD;
`j` (da join) - Il server invia `j <username> <color>` ad ogni client collegato ad un canale dove si è aggiunto un nuovo membro, dove username è il nome utente e color è un codice di 6 cifre esadecimali che rappresenta il colore associato all'utente;
`l` (da leave) - Il server invia `l <username>` ad ogni client collegato ad un canale dove un membro ha appena abbandonato;
`u` (da update) - Il server invia `u <username> <color>` ad ogni client collegato ad un canale dove un utente ha deciso di modificare il suo colore associato;
`d` (da data) - Il server invia `d <n> ` seguito da $n$ coppie di `<username> <color>` intervallate da spazi singoli, a indicare gli utenti presenti in un canale;
`e` (da error) - Il server invia `e <stringa di errore>` in caso di un qualunque errore.