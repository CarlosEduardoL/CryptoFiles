# CryptoFiles
![](md_images/preview.jpeg)
En el proyecto se realizó la interfaz gráfica usando JavaFX, que a través de un FileChooser seleccionar un archivo,
y un campo de texto que permite ingresar un passphrase, usando el algoritmo PBKDF2, convirtiendolo en una llave para el algoritmo AES.
El programa permite dos modos: Encripción y desencripción.

## Encripción
- Toma el archivo y se calcula su SHA1 correspondiente.
- Convierte el archivo en un Fileinputstream
- Se crea un outputstream con un archivo que será el destino del cifrado con extensión .encrypt.
- Se almacena en este archivo la SHA1 del archivo desencriptado.
- Usando un Cipheroutputstream se cifra el outputstream del archivo cifrado.
- Se pasa la información del Fileinputstream al Cipheroutputstream


## Desencripción
- Se lee la SHA1 almacenada en el archivo cifrado.
- Se pasa el Inputstream del archivo cifrado al método de desencripción.
- Se desencripta el archivo usando un Cipheroutputstream.
- Se calcula el SHA1 del archivo desencriptado y se compara.


##Problemas encontrados

- No se calculaba el SHA1 de los archivos
- Debido a que se cargaba todo el archivo a memoria cuando se encriptaba,
   con archivos muy grandes el programa dejaba el computador inutilizable. por lo que el metodo de encripción ahora usa un stream.
- Debido a que se presentaba la misma situación en la desencripción, se optó por usar un stream.