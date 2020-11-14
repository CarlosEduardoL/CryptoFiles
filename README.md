# CryptoFiles
![](md_images/preview.jpeg)
En el proyecto se realizó la interfaz gráfica en java, junto con su correspondiente controlador
en kotlin. Ya se hicieron los métodos de encripción y desencripción,
que transforman un array de bytes desencriptado en uno encriptado.
Con ayuda de la interfaz gráfica leemos un archivo, leemos todos sus bytes,
los encriptamos y los escribimos en otro archivo. De la misma manera, también
se pueden leer todos los bytes del archivo que se seleccione y desencriptarlos.
Todo esto usando una llave generada con una contraseña (en texto plano) con el
algoritmo PBKDF2
Debido a que se cargaba todo el archivo a memoria cuando se encriptaba,
 con archivos muy grandes el programa dejaba el computador inutilizable. por lo que el metodo de encri´pcion ahora usa un buffer