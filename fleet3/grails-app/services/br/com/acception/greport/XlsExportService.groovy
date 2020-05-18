package br.com.acception.greport

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DataFormat
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.text.SimpleDateFormat

class XlsExportService {
	
	def grailsApplication
	def sessionFactory
	
	private def tots
	
	
	private def castReportParams(params,reportInstance){
		
		def castValues=[:]
		
		reportInstance.parameters.each{paramInstance->
				if(params.containsKey(paramInstance.name)){
					def rawValue=params[paramInstance.name]
				
				try {
					switch (paramInstance.dataType) {
						case DataType.INTEGER:
							castValues[paramInstance.name]=rawValue as int
							break;
						case DataType.LONG:
							castValues[paramInstance.name]=rawValue as long
							break;
						case DataType.FLOAT:
							castValues[paramInstance.name]=rawValue as float
							break;
						case DataType.DOUBLE:
							castValues[paramInstance.name]=rawValue as double
							break;
						case DataType.DATE:
							castValues[paramInstance.name]=new SimpleDateFormat("dd/MM/yyyy").parse(rawValue)
							break;
						case DataType.STRING:
							castValues[paramInstance.name]=rawValue
							break;

						default:
							break;
						}
					
				} catch (NumberFormatException e) {
					throw new RuntimeException("Relatorio:${reportInstance.name} - Erro ao converter parametro da consulta [${paramInstance.name}] para [${paramInstance.dataType}]")
				}
				
			}
		}
		
		castValues
		
	}
	
	private String formatValue(fieldInstance,rawValue){
		String formatValue
		switch (fieldInstance.dataType) {
			case DataType.FLOAT:
				formatValue=String.format("%.2f",rawValue as float)
				break;
			case DataType.DOUBLE:
				formatValue=String.format("%.2f",rawValue as double)
				break;
			case DataType.DATE:
				formatValue=new SimpleDateFormat("dd/MM/yyyy").format(rawValue)
				break;
			case DataType.TIMESTAMP:
				formatValue=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rawValue)
				break;
			default:
				formatValue=rawValue
				break;
		}
		formatValue
	}
	
	
	
	private def mountOutput(reportInstance,resultSet){
		def rows=[]
		
		/* Prepara colunas de retorno da consulta para relatório */
		resultSet.each{rs->
			
			def row=[:]
			
			if(grailsApplication.isDomainClass(rs.class)){
				
				reportInstance.fields.each{fieldInstance->
					if(rs.properties.containsKey(fieldInstance.name)){
						row[fieldInstance.name]=rs.properties[fieldInstance.name]
						
						if(fieldInstance.totalizer)
							tots[fieldInstance.name]=(tots[fieldInstance.name]?:0)+row[fieldInstance.name]
						
					}
					else
						throw new RuntimeException("Relatorio:${reportInstance.name} - Campo mapeado [${fieldInstance.name}] nao retornado na consulta")
				}
				
				
			} else if(rs instanceof Object[]){
				/* Localiza metainfo de fields pela ordem de retorno na consulta */
				rs.eachWithIndex {val,i->
					def fieldInstance=reportInstance.fields.find{it.order==(i+1)}
					if(fieldInstance){
						row[fieldInstance.name]=val
						
						if(fieldInstance.totalizer)
							tots[fieldInstance.name]=(tots[fieldInstance.name]?:0)+val
					}
					else
						throw new RuntimeException("Relatorio:${reportInstance.name} - Campo de retorno de ordem ${i+1} nao localizado na configuracao")
				}
			}else{
				def fieldInstance=reportInstance.fields.find{it.order==1}
				if(fieldInstance){
					row[fieldInstance.name]=rs
					
					if(fieldInstance.totalizer)
						tots[fieldInstance.name]=(tots[fieldInstance.name]?:0)+rs
				}else
					throw new RuntimeException("Relatorio:${reportInstance.name} - Nao ha configuracao de campo de ordem 1")
			}
			
			if(!row.isEmpty())
				rows<<row
		}
		
		rows
	}
	
	
	def printEmptyLine(sheet,numLine){
		def row=sheet.createRow(numLine)
		return ++numLine
	}
	
	
	def printHeaderFields(fields,wb,sheet,numLine){
		def cell
		def font
		def style
		def row=sheet.createRow(numLine)
		
		fields.sort{it.order}.each{fldInstance->
			
			cell=row.createCell(fldInstance.order)
			cell.setCellValue("${fldInstance.label}")
			/*Formata célula*/
			font=wb.createFont()
			font.setColor(HSSFColor.WHITE.index)
			
			style=wb.createCellStyle()
			style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex())
			style.setFillPattern(CellStyle.BIG_SPOTS)
			style.setFont(font)
			
			cell.setCellStyle(style)
			
		}
		return ++numLine
	}
	
	
	private def applyStyle(wb,cell,weight,points){
		/* Aplica estilo a célula */
		def xFont=wb.createFont()
		xFont.setBoldweight(weight);
		xFont.setFontHeightInPoints((short)points);
		def xStyle=wb.createCellStyle()
		xStyle.setFont(xFont)
		cell.setCellStyle(xStyle)
	}
	
	

    def export(params,outputStream) {

		tots=[:]
		
		/* Recupera dados */		
		def reportInstance=Report.get(params.id)
		
		def castParams=castReportParams(params,reportInstance)
		
		def resultCount
		def resultSet
		
		switch (reportInstance.queryType) {
			case QueryType.SQL:
			
				def hibSession=sessionFactory.currentSession
				
				def query=hibSession.createSQLQuery(reportInstance.query)
				castParams.each{k,v->
					query=query.setParameter(k,v)
				}
				resultSet=query.list()

				break;
				
			case QueryType.HQL:
				resultSet=Report.executeQuery(reportInstance.query,castParams)
				break;
				
			case QueryType.CRITERIA:
			
				break;

			default:
				break;
		}
		
		def rows=mountOutput(reportInstance, resultSet)
		
		/* Monta XLS*/
		XSSFWorkbook wb=new XSSFWorkbook()
		XSSFSheet sheet=wb.createSheet()
		
		Row xRow
		Cell xCell
		
		CellStyle xStyle
		Font xFont
		
		DataFormat xFormat=wb.createDataFormat()

		def currGroup=[:]
		
		def groupFields=reportInstance.fields.findAll{it.groupBy}
		
		def noGrpFields=reportInstance.fields.findAll{!it.groupBy}
		
		/* Inicializa mapa para defCampos de agrupamento */
		groupFields.each{fld->
			currGroup[fld]=""
		}
		
		def numLine=0
		
		/* Imprime cabeçalho com nome do relatório*/
		
		xRow=sheet.createRow(numLine++)
		xCell=xRow.createCell(1)
		xCell.setCellValue("${reportInstance.name}")
		
		applyStyle(wb,xCell,Font.BOLDWEIGHT_BOLD,12)
		
		
		/* Imprime linhas de parâmetros */
		
		reportInstance.parameters.sort{it.order}.each{paramInstance->
			if(params.containsKey(paramInstance.name)){
				
				xRow=sheet.createRow(numLine++)
				
				xCell=xRow.createCell(1)
				xCell.setCellValue("${paramInstance.label}")
				applyStyle(wb,xCell,Font.BOLDWEIGHT_BOLD,10)
				
				xRow.createCell(2).setCellValue("${params[paramInstance.name]}")
				
			}
		}
		
		numLine=printEmptyLine(sheet,numLine)
		
		/* Imprime cabeçalho de defCampos quando não existes agrupamentos */
		if(groupFields.isEmpty()){
			numLine=printHeaderFields(noGrpFields,wb,sheet,numLine)
		}
		
		
		/* Imprime Linhas do ResultSet */
		rows.each{row->
			
			/* Linha de quebra por coluna de agrupamento */
			if(!groupFields.isEmpty()){
				
				def nobreak=true
				
				/* Teste de quebra */
				currGroup.each{k,v->
					nobreak=nobreak && (formatValue(k,row[k.name])==v)
				}
				
				/* Linha de cabeçalho de grupo */
				if(!nobreak){
					/* Atualiza valores do controle de quebra */
					groupFields.each{fld->
						currGroup[fld]=formatValue(fld,row[fld.name])
					}
					
					/* pula linha */
					numLine++
					
					xRow=sheet.createRow(numLine++)
					groupFields.eachWithIndex{field,ind->
						
						xCell=xRow.createCell(ind)
						xCell.setCellValue("${field.label}:  ${formatValue(field,row[field.name])}")
						
						applyStyle(wb,xCell,Font.BOLDWEIGHT_BOLD,11)
						
					}
					xRow=sheet.createRow(numLine++)
					
					noGrpFields.sort{it.order}.eachWithIndex{fldInstance,i->
						
						xCell=xRow.createCell(i+1)
						xCell.setCellValue("${fldInstance.label}")
						/*Formata célula*/
						xFont=wb.createFont()
						xFont.setColor(HSSFColor.WHITE.index)
						
						xStyle=wb.createCellStyle()
						xStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex())
						xStyle.setFillPattern(CellStyle.BIG_SPOTS)
						xStyle.setFont(xFont)
						
						xCell.setCellStyle(xStyle)
						
					}
					
				}
			}
			
			xRow=sheet.createRow(numLine++)
			
			noGrpFields.sort{it.order}.eachWithIndex{fieldInstance,i->
				xCell=xRow.createCell(i+1)
				
				if(fieldInstance.dataType==DataType.DATE)
					xCell.setCellValue(new SimpleDateFormat("dd/MM/yyyy").parse(row[fieldInstance.name]))
				else if(fieldInstance.dataType==DataType.TIMESTAMP)
					xCell.setCellValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(row[fieldInstance.name]))
				else if(fieldInstance.dataType in [DataType.FLOAT,DataType.DOUBLE]){
					xCell.setCellValue(row[fieldInstance.name])
					xStyle=wb.createCellStyle();
					xStyle.setDataFormat(xFormat.getFormat("#,##0.00"));
					xCell.setCellStyle(xStyle)
				}else if(fieldInstance.dataType==DataType.STRING)
					xCell.setCellValue(row[fieldInstance.name] as String)
				else
					xCell.setCellValue(row[fieldInstance.name])
				
			}
	
			
		}
		
		/* Imprime Totalizadores */
		
		def totalizers=reportInstance.fields.findAll{it.totalizer}
		
		totalizers.each{totField->
			xRow=sheet.createRow(numLine++)
			xRow.createCell(noGrpFields.size()-2).setCellValue("Total de ${totField.label}:")
			xRow.createCell(noGrpFields.size()-1).setCellValue("${tots[totField.name]}")
		}
		
		wb.write(outputStream)
		
    }
}
