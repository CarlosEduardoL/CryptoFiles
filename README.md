# CryptoFiles
<center>

![](md_images/preview3.jpeg)
</center>
<br/>


En el proyecto se realizó la interfaz gráfica usando JavaFX, que a través de un FileChooser seleccionar un archivo,
y un campo de texto que permite ingresar un passphrase, usando el algoritmo [PBKDF2](https://github.com/CarlosEduardoL/CryptoFiles/blob/bae10121ed6a372abdc957f9f713f8a7566f10aa/src/main/kotlin/util/CryptoUtils.kt#L29), convirtiendolo en una llave para el algoritmo AES.
El programa permite dos modos: Encripción y desencripción.

## Encripción
- Toma el archivo y se calcula su [SHA1](src/main/kotlin/util/Utils.kt) correspondiente.
- Convierte el archivo en un Fileinputstream
- Se crea un outputstream con un archivo que será el destino del cifrado con extensión .encrypt.
- Se almacena en este archivo la SHA1 del archivo desencriptado.
- Usando un Cipheroutputstream se cifra el outputstream del archivo cifrado.
- Se pasa la información del Fileinputstream al Cipheroutputstream


## Desencripción
- Se lee la SHA1 almacenada en el archivo cifrado.
- Se pasa el Inputstream del archivo cifrado al método de desencripción.
- Se desencripta el archivo usando un Cipheroutputstream.
- Se calcula el [SHA1](src/main/kotlin/util/Utils.kt) del archivo desencriptado y se compara.


## Problemas encontrados

- No se calculaba el SHA1 de los archivos, solucionado con [esto](src/main/kotlin/util/Utils.kt)
- Debido a que se cargaba todo el archivo a memoria cuando se encriptaba,
   con archivos muy grandes el programa dejaba el computador inutilizable. por lo que el metodo de encripción ahora usa un stream.
- Debido a que se presentaba la misma situación en la desencripción, se optó por usar un stream.
- No habia feedback que reflejara que ya habian sido terminados los procesos. Lo solucionamos poniendo dialogos al final de cada proceso y también, pusimos una barra de progreso.
- Los botones no se bloqueaban durante el proceso de encripción o desencripción, así que la persona podía poner a encriptar o desencriptar varias veces el mismo archivo. Los solucionamos bloqueando los botones durante los procesos. 
- Solo podemos cifrar archivos cifrados con nuestro programa, estos tienen una extensión especial (.encrypt) dado esto y para simplificar la interfaz dejamos un solo botón que cambia segun si el archivo es .encrypt o no y efectua el proceso de encriptar o desencriptar. 

## Ejemplo de uso

### _**Encripción**_
- Se selecciona el archivo a encriptar
<br/>

![](md_images/enc2.5.JPG)

- Se preciona el boton
<br/>

![](md_images/enc1.jpeg)

- Se espera a que el programa calcule la SHA-1
<br/>

![](md_images/enc2.jpeg)

- se espera a que termine el proceso de encripcion
<br/>

![](md_images/enc3.jpeg)

- el programa da aviso de la correcta finalizacion
<br/>

![](md_images/enc4.jpeg)
<br/>
<br/>

### _**Desencripción**_

- Se selecciona el archivo para desencriptar. Extension .encrypt
<br/>

![](md_images/enc2.5.JPG)

- Se preciona el boton de desencripcion
<br/>

![](md_images/dec1.jpeg)

- Se espera al final del proceso de desencripcion
<br/>

![](md_images/dec2.jpeg)

- Se comprueban los SHA-1 calculados por el sistema
<br/>

![](md_images/dec3.jpeg)