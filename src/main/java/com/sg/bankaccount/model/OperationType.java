package com.sg.bankaccount.model;

public enum OperationType {

    DEPOSIT("deposit operation", 1),
    WITHDRAW("withdraw operation", 2);

    private final String operationName;
    private final int operationId;

    OperationType(String operationName, int operationId) {
        this.operationName = operationName;
        this.operationId = operationId;
    }

    public int getOperationId() {
        return operationId;
    }
}
