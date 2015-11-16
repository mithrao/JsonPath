package com.jayway.jsonpath.internal.filter;

import com.jayway.jsonpath.internal.Utils;

import java.util.ArrayList;
import java.util.List;

public class LogicalExpressionNode extends ExpressionNode {
    protected List<ExpressionNode> chain = new ArrayList<ExpressionNode>();
    private final LogicalOperator operator;

    public static LogicalExpressionNode createLogicalOr(ExpressionNode left,ExpressionNode right){
        return new LogicalExpressionNode(left, LogicalOperator.OR, right);
    }

    public static LogicalExpressionNode createLogicalAnd(ExpressionNode left,ExpressionNode right){
        return new LogicalExpressionNode(left, LogicalOperator.AND, right);
    }

    private LogicalExpressionNode(ExpressionNode left, LogicalOperator operator, ExpressionNode right) {
        chain.add(left);
        chain.add(right);
        this.operator = operator;
    }

    public LogicalExpressionNode and(LogicalExpressionNode other){
        return createLogicalAnd(this, other);
    }

    public LogicalExpressionNode or(LogicalExpressionNode other){
        return createLogicalOr(this, other);
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public LogicalExpressionNode append(ExpressionNode expressionNode) {
        chain.add(0, expressionNode);
        return this;
    }

    @Override
    public String toString() {
        return "(" + Utils.join(" " + operator.getOperatorString() + " ", chain) + ")";
    }

    @Override
    public boolean apply(PredicateContext ctx) {
        if(operator == LogicalOperator.OR){
            for (ExpressionNode expression : chain) {
                if(expression.apply(ctx)){
                    return true;
                }
            }
            return false;
        } else {
            for (ExpressionNode expression : chain) {
                if(!expression.apply(ctx)){
                    return false;
                }
            }
            return true;
        }
    }
}