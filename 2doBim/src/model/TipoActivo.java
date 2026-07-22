package model;

/**
 * Tipos de activo soportados. Sirve para discriminar el tipo en la BD
 * y en reportes, sin tener que usar instanceof en las capas superiores.
 */
public enum TipoActivo {
    HARDWARE,
    PERIFERICO,
    LICENCIA
}