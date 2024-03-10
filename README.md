# pruebas-entrega2
Segundo entregable de Pruebas y Despliegue de Software.

## Valoración
**NOTA: 7**

### Comentarios
Las clases de equivalencia de los plazos no son correctas: "Fecha entre cierre y celebración de la carrera". En ese margen de tiempo, hay días en que la inscripción es válida (desde Cierre hasta 3 días antes de la carrera) y días en que no (los dos días previos a la carrera).

Se identifica CE de edad raras: "Menor de 60", "Menor de 65"...

No se identifican CEs para el registro de atletas.

Aporta trazabilidad pero sin el diagrama exigido.

Se dice que aplica AVL en los plazos de inscripción pero en las SPs no se aprecia que se inscriba
 el primer día de plazo, el último... (SPI.2)

Prácticamente la totalidad de los CPs cubren una única situación de prueba. Sólo hay 4 casos
 en los que se cubren más SPs.

No detecta fallo en la cuota asignada cuando el plazo 2 no existe.
