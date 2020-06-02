/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.sysdata.gestaofrota


import java.text.SimpleDateFormat

class AuditLogEventController {

  // the delete, save and update actions only accept POST requests
  static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

  def index() {
    redirect(action: 'list', params: params)
  }


  def beforeInterceptor=[
      action:{
        def df=new SimpleDateFormat("dd/MM/yyyy")
        params.data=(params.data)?df.parse(params.data):null
      },
      only:['list']
  ]


  def list() {
    params.max=Math.min(params.max ? params.int('max') : 10, 100)
    params.offset=params.offset?params.offset as int:0

    def auditLogEventInstanceList=AuditLogEvent.withCriteria {
                                      if(params.usuario)
                                        eq("actor",params.usuario)
                                      if(params.data)
                                        between('dateCreated',params.data,params.data+1)

                                        order("id","desc")
                                        maxResults(params.max)
                                        firstResult(params.offset)

                                      }

    def auditLogEventInstanceTotal=AuditLogEvent.withCriteria {
                                      if(params.usuario)
                                        eq("actor",params.usuario)
                                      if(params.data)
                                        between('dateCreated',params.data,params.data+1)

                                        projections{rowCount()}
                                      }[0]


    [auditLogEventInstanceList:auditLogEventInstanceList, auditLogEventInstanceTotal:auditLogEventInstanceTotal,params:params]
  }

  def show() {
    def auditLogEvent
    // GPAUDITLOGGING-81: As the id type is configurable in the config, the attribute type is Object in AuditLogEvent.
    // This causes the auto conversion not to work anymore. As we haven't found a way to get the mapping type in an ORM-agnostic way,
    // we simply cast to Long as a first try and use String as the 2nd. Other conversions currently not supported.
    // We badly need GH #13 implemented.
    try {
      auditLogEvent = AuditLogEvent.get(params.long('id'))
    } catch (Exception e){
      try {
        auditLogEvent = AuditLogEvent.get(params.id)
      } catch (Exception giveup){
        log.error("Cannot obtain AuditLogEvent. ", giveup)
      }
    }
    if (auditLogEvent == null) {
      flash.message = "AuditLogEvent not found with id ${params.id}"
      redirect(action: 'list')
      return
    }


    [auditLogEventInstance: auditLogEvent]
  }

  def delete() {
    redirect(action: 'list')
  }

  def edit() {
    redirect(action: 'list')
  }

  def update() {
    redirect(action: 'list')
  }

  def create() {
    redirect(action: 'list')
  }

  def save() {
    redirect(action: 'list')
  }
}
