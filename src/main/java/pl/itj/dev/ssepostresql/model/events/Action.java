/* (C)2022 */
package pl.itj.dev.ssepostresql.model.events;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Action {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete");

    private final String operation;

    Action(String operation) {
        this.operation = operation;
    }
}
