package com.sysdata.commons.dates

import com.sysdata.gestaofrota.Cidade
import grails.gorm.transactions.Transactional

@Transactional
class HolidayService {

    boolean isMunicipalHoliday(Date date, Cidade cidade) {
        return MunicipalHolidayDate.withCriteria {
            holiday {
                eq("city", cidade)
            }
            eq("date", date.clearTime())
        }.size() > 0
    }

    boolean isStateHoliday(Date date, Cidade cidade) {
        return StateHolidayDate.withCriteria {
            holiday {
                eq("state", cidade.estado)
            }
            eq("date", date.clearTime())
        }.size() > 0
    }

    boolean isNationalHoliday(Date date) {
        return NationalHolidayDate.withCriteria {
            eq("date", date.clearTime())
        }.size() > 0
    }

    def isHoliday(Date date, Cidade city) {
        if (!city)
            throw new RuntimeException("Cidade precisa ser informada para checagem de feriado")

        return isMunicipalHoliday(date, city) || isStateHoliday(date, city) || isNationalHoliday(date)
    }
}
