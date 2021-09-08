package com.sysdata.commons.dates

import grails.gorm.transactions.Transactional

@Transactional
class HolidayService {

    def isHoliday(Date date) {
        return HolidayDate.findWhere(date: date) != null
    }
}
