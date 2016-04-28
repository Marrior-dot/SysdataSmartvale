import com.sysdata.gestaofrota.*
import com.sysdata.com.modelo.*
import com.sysdata.com.modelo.types.*

def log(msg){

    println msg
        
    def logfile=new File("/home/diego/lancamentos com dt reembolso errados frota.log")
    if(logfile.exists())
        logfile.append("\n"+msg)    
    else
        logfile.write(msg)    
}

def hibSess=ctx.getBean("sessionFactory").currentSession
def today=new Date()
def refCort=[:]

log "Recuperando lancamentos errados..."
// "Recuperando lancamentos errados..."
def lancamentoList=hibSess.createSQLQuery("""select data_efetivacao,id from lancamento l where status = 'A_EFETIVAR' AND tipo='REEMBOLSO' ORDER BY ID DESC""").list()

//select  p.nome, cast(l.data_efetivacao as date), l.valor from lancamento l, conta c, participante p where l.conta_id = c.id and c.id=p.conta_id and l.status = 'A_EFETIVAR' AND l.tipo='REEMBOLSO' and extract ('dow' from l.data_efetivacao) in (0,6) 
//and class = 'com.sysdata.gestaofrota.PostoCombustivel' ORDER BY l.data_efetivacao desc

Date date
java.sql.Timestamp sq
lancamentoList.each{bol->

    log "LAN=[lcn_id:${bol[1]} dtEfet:${bol[0]}]"
    //select data_efetivacao from lancamento l where status = 'A_EFETIVAR' AND tipo='REEMBOLSO' and id=3178 ORDER BY ID DESC
  date = new Date(bol[0].getTime());
  Calendar c = Calendar.getInstance();
  c.setTime(date);
  int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
  log "Dia da semana ${dayOfWeek}"
  def l = Lancamento.get(bol[1])
  log "data efetivacao antes da mudanca:  ${l.dataEfetivacao}"
  if(dayOfWeek == 7){
                
       date+=2
       log "Nova data do if ${date}"
       sq = new java.sql.Timestamp(date.getTime());
       log "Data em Timestamp ${sq}"
       l.dataEfetivacao=sq
       log "data efetivacao depois da mudanca: ${l.dataEfetivacao}"
                
  }else if (dayOfWeek == 1){
            
       date+=1
       log "Nova data do else ${date}"
       sq = new java.sql.Timestamp(date.getTime());
       log "Data em Timestamp ${sq}"
       l.dataEfetivacao=sq
       log "data efetivacao depois da mudanca: ${l.dataEfetivacao}"
                            
  }



}