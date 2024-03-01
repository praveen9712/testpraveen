
package com.qhrtech.emr.restapi.security;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.util.Assert;

/**
 *
 * @author Blake Dickie
 */
public class AccuroSecurityExpressionParser implements ExpressionParser {

  private final ExpressionParser delegate;

  public AccuroSecurityExpressionParser(ExpressionParser delegate) {
    Assert.notNull(delegate, "delegate cannot be null");
    this.delegate = delegate;
  }

  @Override
  public Expression parseExpression(String expressionString) throws ParseException {
    return delegate.parseExpression(wrapExpression(expressionString));
  }

  @Override
  public Expression parseExpression(String expressionString, ParserContext context)
      throws ParseException {
    return delegate.parseExpression(wrapExpression(expressionString), context);
  }

  private String wrapExpression(String expressionString) {
    return "#emr.throwOnError(" + expressionString + ")";
  }

}
