package com.outr.query.search

import com.outr.query.table.property.TableProperty
import com.outr.query.Table
import com.outr.query.h2.Triggers
import com.outr.query.h2.trigger.TriggerEvent
import org.powerscala.search.{DocumentUpdate, Search}
import com.outr.query.orm.ORMTable
import org.powerscala.log.Logging

/**
 * @author Matt Hicks <matt@outr.com>
 */
trait Searchable extends TableProperty {
  def name = Searchable.name

  def updateDocument(search: Search, evt: TriggerEvent): Unit

  def deleteDocument(search: Search, evt: TriggerEvent): Unit

  override def addedTo(table: Table) = {
    super.addedTo(table)

    if (!table.has(Triggers.name)) {    // Make sure triggers are enabled on the table
      table.props(Triggers.Normal)
    }
  }
}

trait BasicSearchable extends Searchable {
  def event2DocumentUpdate(evt: TriggerEvent): DocumentUpdate

  override def updateDocument(search: Search, evt: TriggerEvent) = search.update(event2DocumentUpdate(evt))

  override def deleteDocument(search: Search, evt: TriggerEvent) = search.delete(event2DocumentUpdate(evt))
}

trait ORMSearchable[T] extends Searchable with Logging {
  def toDocumentUpdate(t: T): DocumentUpdate

  override def updateDocument(search: Search, evt: TriggerEvent) = {
    val table = evt.table.asInstanceOf[ORMTable[T]]
    val idColumn = table.primaryKeys.head
    val id = evt(idColumn)
    table.byId(id) match {
      case Some(t) => {
        val update = toDocumentUpdate(t)
        search.update(update)
      }
      case None => warn(s"Unable to find instance in ${table.tableName} by id: $id.")
    }
  }

  override def deleteDocument(search: Search, evt: TriggerEvent) = {
    val table = evt.table.asInstanceOf[ORMTable[T]]
    val idColumn = table.primaryKeys.head
    val id = evt(idColumn)
    table.byId(id) match {
      case Some(t) => {
        val update = toDocumentUpdate(t)
        search.delete(update)
      }
      case None => warn(s"Unable to find instance in ${table.tableName} by id: $id.")
    }
  }
}

object Searchable {
  val name = "searchable"
}