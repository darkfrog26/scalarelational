package org.scalarelational.instruction.ddl

import org.scalarelational.table.Table
import org.scalarelational.column.Column
import org.scalarelational.instruction.CallableInstruction

import scala.language.implicitConversions

/**
 * @author Matt Hicks <matt@outr.com>
 */
trait DDLSupport {
  def table2Create(table: Table[_], ifNotExists: Boolean = true): CreateTable
  def column2Create[T](column: Column[T]): CreateColumn[T]

  def ddl(tables: List[Table[_]], ifNotExists: Boolean = true): List[CallableInstruction]

  def ddl(create: CreateTable): List[CallableInstruction]

  def ddl[T](create: CreateColumn[T]): List[CallableInstruction]

  def ddl(alter: CreateForeignKey): List[CallableInstruction]

  def ddl(create: CreateIndex): List[CallableInstruction]

  def ddl(alter: RenameColumn): List[CallableInstruction]

  def ddl(alter: RestartColumn): List[CallableInstruction]

  def ddl[T](alter: ChangeColumnType[T]): List[CallableInstruction]

  def ddl(drop: DropTable): List[CallableInstruction]

  def ddl(drop: DropColumn): List[CallableInstruction]

  def ddl(drop: DropIndex): List[CallableInstruction]
}