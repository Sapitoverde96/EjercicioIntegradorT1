# Ejercicio Integrador T1
Creado por *Valentin Leyton Golla* y *Ciro Álvarez Garrido*. Alumnos de 2DAM, Grupo1.

El siguiente proyecto es una aplicación de línea de comandos (CLI) para gestionar una lista de candidaturas correspondientes a varias empresas. 
Es posible listarlas, añadir nuevas, eliminar existentes e importarlas desde un archivo CSV.

## Requisitos
- **Java 27** o superior
- **Maven 4.0.0** o superior

## Instalación
Para descargar el siguiente proyecto, ejecuta estos comandos en la terminal:
```bash
git clone https://github.com/Sapitoverde96/EjercicioIntegradorT1.git
cd EjercicioIntegradorT1
mvn clean install
```

## Uso
### Para la versión 1.0
El programa presente una variedad de opciones para gestionar archivos de tipo `.csv` que contenga candidaturas. 
Es muy importante que los ficheros tengan el siguiente formato `Id;Titulo;Estado;Empresa;FechaDeCreación`,
de lo contrario, el programa no funcionara correctamente. Asegurate de encontrarte en la carpeta del proyecto (puedes moverte con `cd`) antes de ejecutar el siguiente comando:
```bash
java -cp target/classes com.cesur.valciro.Run
```
*Este comando es necesario que se ejecute siempre antes de añadir los demás argumentos.*

---

**LISTAR**  
`listar --estado X --empresa Y`  
Los parámetros estado y empresa son totalmente opcionales y no tienen un orden especifico. Cuando solo se escribe listar, se devuelve todas las candidaturas del archivo actual en uso.  
- `--estado` **:** Filtra las candidaturas por su estado, solo acepta *accepted*, *pending* o *rejected*. En caso de usar otro valor, se lanzará un error.
- `--empresa` **:** Filtra las candidaturas por la empresa a la que aplican. Esta pensado para ser similar a muchas aplicaciones comerciales y muestra todos las coincidencias similares en vez de ser totalmente estricto. Es decir, si el usuario escribe `goo` como valor, se mostraran todas las candidaturas que contengan `goo` dentro del nombre de la empresa.

---

**AÑADIR**  
`añadir --titulo X --estado Y --empresa Z --fecha DD/MM/YYYY`  
Todos los parámetros son totalmente obligatorios y se deben seguir en el orden observado. Cuando todos son válidos, añade una nueva candidatura con esos datos al fichero csv en uso.
- `--titulo` **:** Establece el nombre de la candidatura, por ejemplo *Lead UX/UI Designer*.
- `--estado` **:** Establece el estado de la candidatura visto anteriormente en **listar**, sigue el mismo patrón, solo acepta *accepted*, *pending* o *rejected*.
- `--empresa` **:** Establece la empresa a la que se aplica en la candidatura, por ejemplo *Wallapop*.
- `--fecha` **:** Establece la fecha en la que se ha creado la candidatura, por ejemplo *02/03/2026*. Solo acepta fechas que sigan el modelo dd/mm/yyyy.

---

**VER**  
`ver X` [X es un número sin decimales]  
Muestra una candidatura en concreto con todos sus datos asociados. La búsqueda es por su identificador numérico, el programa se ha pensado para ser amigable con todos los tipos de usuarios y los identificadores empiezan por 1 en vez de 0. Cualquier valor que no sea un número no es válido y lanzará un error.

---

**BORRAR**  
`borrar X` [X es un número sin decimales]  
Elimina una candidatura en concreto con todos sus datos asociados. La búsqueda es por su identificador numérico, el programa se ha pensado para ser amigable con todos los tipos de usuarios y los identificadores empiezan por 1 en vez de 0. Cualquier valor que no sea un número no es válido y lanzará un error.

---

**IMPORTAR**  
`importar X` [X es el nombre de un fichero y su extensión]  
Cambia el fichero en el que se trabaja al especificado por el usuario en el comando. Importar un fichero cambiara permanentemente a ese fichero en el resto de ejecuciones hasta que el usuario decida cambiarlo. En caso de querer cambiar a mano, `importar` cambia el fichero de lectura en el archivo `config.txt`, editar manualmente su contenido al nombre deseado tendrá el mismo efecto. A tener en cuenta que no se admiten archivos que no sean de extensión `.csv` y el programa lanzará un error en tal caso.

## Contribuciones
El programa de CLI ha sido principalmente desarrollada por Valentin Leyton Golla y el Figma por Ciro Álvarez Garrido. Estrictamente hablando del programa, el reparto fue el siguiente:

**Ciro Álvarez Garrido** ha hecho principalmente *Guardar candidaturas al CSV en Repository*, *Convertir objetos candidaturas a strings CSV en Repository*, *Listar candidaturas en Service*, *Añadir candidaturas en Service*, *Ver candidaturas en Service*, *Generar ID en Service* y por último las funciones de *Controller* relacionadas con ejecutar *Ver*, *Borrar* e *Importar* que recogen los argumentos e imprimen en la terminal.

**Valentin Leyton Golla** ha hecho todas las demás clases y funciones que incluyen la *Entidad y su enum*, *Config y su Repository*, *todo lo que queda en el Repository, Service y Controller de las Candidaturas junto a sus métodos helpers*, el debugging y testeo, una refactorización masiva del código de todos los integrantes, documentación *Javadoc* del proyecto y la organización de diseño y arquitectura. Además de este README.md.

*La información está más detallada en todos los commits que se han hecho hasta la fecha*