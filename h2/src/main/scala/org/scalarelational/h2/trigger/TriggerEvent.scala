package org.scalarelational.h2.trigger

import scala.language.existentials

import org.scalarelational.table.Table
import org.scalarelational.column.Column

/**
 * @author Matt Hicks <matt@outr.com>
 */
case class TriggerEvent(table: Table[_], triggerType: TriggerType, state: TriggerState, oldRow: Array[AnyRef], newRow: Array[AnyRef]) {
  def apply[T](column: Column[T], array: Array[AnyRef] = defaultArray) = column.dataType.fromSQLType(column, defaultArray(column.index))

  def defaultArray = if (triggerType == TriggerType.Delete) {
    oldRow
  } else {
    newRow
  }
}
