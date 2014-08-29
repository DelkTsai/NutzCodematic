package pandy.com;

/**
 * Created by IntelliJ IDEA.
 * User: Admin12
 * Date: 2005-12-7
 * Time: 15:01:33
 * To change this template use File | Settings | File Templates.
 */
/*����JFreeChart 1.0.0 Pre2.jar*/
import java.io.*;
import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.jfree.chart.*;
//���ݼ�
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.WindDataset;
import org.jfree.data.general.WaferMapDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.SignalsDataset;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;

//���ݼ�����
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

//ͼ��
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.XYPlot;


//������ʽ��
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineRenderer3D;
import org.jfree.chart.renderer.category.WaterfallBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

//��ǩ
import org.jfree.chart.labels.StandardPieItemLabelGenerator;


//�̶�
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.SegmentedTimeline;


//ͨ��
import org.jfree.data.general.DatasetUtilities;

//����
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.util.TableOrder;


public class FreeChart
{
 /*�������ͼ�������Dataset*/
 private DefaultCategoryDataset vDefaultCategoryDataset=null;
 private CategoryDataset vCategoryDataset=null;
 private DefaultPieDataset vDefaultPieDataset=null;
 private PieDataset vPieDataset=null;
 private XYDataset vXYDataset=null;
 private TableXYDataset vTableXYDataset=null;
 private XYZDataset vXYZDataset=null;
 private IntervalXYDataset vIntervalXYDataset=null;
 private WindDataset vWindDataset=null;
 private WaferMapDataset vWaferMapDataset=null;
 private IntervalCategoryDataset vIntervalCategoryDataset=null;
 private OHLCDataset vOHLCDataset=null;
 private SignalsDataset vSignalsDataset=null;
 private BoxAndWhiskerXYDataset vBoxAndWhiskerXYDataset=null;
 /*�������ͼ�������Dataset����*/
 TimeSeries vTimeSeries=null;
 TimeSeriesCollection vTimeSeriesCollection=null;
 /*����ͼ��*/
 private JFreeChart vFreeChart=null;
 private JFreeChart vFreeChartExtend=null;
 /*ӳ��ͼƬ��·������*/
 private String strFileName=null;

 /*����Ч��*/
 private CategoryPlot vCategoryPlot=null;
 private PiePlot vPiePlot=null;
 private MultiplePiePlot vMultiplePiePlot=null;
 private PiePlot3D vPiePlot3D=null;
 private XYPlot vXYPlot=null;

 private StandardPieItemLabelGenerator vStandardPieItemLabelGenerator=null;

 /*����̶�*/
 private NumberAxis vNumberAxis=null;
 private DateAxis vDateAxis=null;
 private CategoryAxis vCategoryAxis=null;
 private ValueAxis vValueAxis=null;
 private SegmentedTimeline vSegmentedTimeline=null;

 private BarRenderer vBarRenderer=null;
 private BarRenderer3D vBarRenderer3D=null;
 private StackedBarRenderer vStackedBarRenderer=null;
 private StackedBarRenderer3D vStackedBarRenderer3D=null;
 private StackedAreaRenderer vStackedAreaRenderer=null;
 private LineAndShapeRenderer vLineAndShapeRenderer=null;
 private LineRenderer3D vLineRenderer3D=null;
 private WaterfallBarRenderer vWaterfallBarRenderer=null;
 private XYItemRenderer vXYItemRenderer=null;

 //�Ƿ�����Ч��ģʽ
 private boolean bRender=false;

 /*���徲̬����*/
 private String[] strItemArray=null;
 private String[] strMultipleItemArray=null;
 private String[] strCategoryArray=null;
 private String[] strDataArray=null;
 private String[] strMultipleDataArray=null;
 private double[][] iMultipleDataArray=null;

 /**/

 public static String[] vChartTypeArray={"��ͼ","�����ͱ�ͼ","��ά��ͼ","���ϱ�ͼ","��ά�����ͱ�ͼ","��״����ͼ","��ά��״����ͼ","�ѻ�����ͼ","��ά�ѻ�����ͼ","���ͼ","��ά���ͼ","����ͼ","��ά����ͼ","��ά����ͼ","��ά����ͼ","�״�ͼ","ɢ��ͼ","�ٷֱȶѻ����ͼ","��ά�ٷֱȶѻ����ͼ","����ɢ��ͼ","���ɢ��ͼ","ʱ������ͼ","��ά����Բ׶ͼ","�̸�-�̵�-����ͼ","����-�̸�-�̵�-����ͼ","����ͼ�����ӿ��ͼ��","����ͼ","��״����ͼ","���ݵ�����ͼ","�����ݵ�����ɢ��ͼ","�����ݵ�ƽ����ɢ��ͼ"};
 public String strTimeAxis="";
 public void FreeChart()
 {

 }
 public void createDataset(String strMultipleItemList,String strCategoryList,String strMultipleDataList)
 {
  strMultipleItemArray=strMultipleItemList.split(",");
  strCategoryArray=strCategoryList.split(",");
  strMultipleDataArray=strMultipleDataList.split("#");
  iMultipleDataArray=new double[strCategoryArray.length][strMultipleItemArray.length];

  java.text.SimpleDateFormat vSimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
  java.text.SimpleDateFormat vSimpleDateFormatToday=new SimpleDateFormat("yyyy-MM-");
  java.util.Date vItemDate=null;
  java.util.Date vTodayDate=null;
  Day vDay=null;
  vTodayDate=new java.util.Date();
  vTimeSeriesCollection=new TimeSeriesCollection();
  vTimeSeriesCollection.setDomainIsPointsInTime(false);

  for(int mIndex=0;mIndex<strMultipleDataArray.length;mIndex++)
  {
   String[] strElementArray=strMultipleDataArray[mIndex].split(",");
   if(strTimeAxis.equals("Hour"))
   {
    vTimeSeries=new TimeSeries(strCategoryArray[mIndex],Hour.class);
   }
   else
   {
    vTimeSeries=new TimeSeries(strCategoryArray[mIndex],Day.class);
   }
   //vTimeSeries.clear();
   for(int nIndex=0;nIndex<strElementArray.length;nIndex++)
   {
    iMultipleDataArray[mIndex][nIndex]=Double.parseDouble(strElementArray[nIndex]);
    try
    {
     //vItemDate=vSimpleDateFormat.parse(vSimpleDateFormatToday.format(vTodayDate)+String.valueOf(nIndex+1));
     vItemDate=vSimpleDateFormat.parse(strMultipleItemArray[nIndex]);
     if(strTimeAxis.equals("Hour"))
     {
      vDay=new Day(vItemDate.getDate(),1+vItemDate.getMonth(),1900+vItemDate.getYear());
      vTimeSeries.add(new Hour(vItemDate.getHours(),vDay),Double.parseDouble(strElementArray[nIndex]));
     }
     else
     {
      vTimeSeries.add(new Day(vItemDate.getDate(),1+vItemDate.getMonth(),1900+vItemDate.getYear()),Double.parseDouble(strElementArray[nIndex]));
     }

    }
    catch(Exception e)
    {System.out.println(e.getMessage());}
   }
   vTimeSeriesCollection.addSeries(vTimeSeries);
  }
  try
  {
   vCategoryDataset=DatasetUtilities.createCategoryDataset(strCategoryArray,strMultipleItemArray,iMultipleDataArray);
   vPieDataset=DatasetUtilities.createPieDatasetForColumn(vCategoryDataset,0);
   vPieDataset=DatasetUtilities.createPieDatasetForRow(vCategoryDataset,0);
   //vWaferMapDataset=(WaferMapDataset)vCategoryDataset;

   vTableXYDataset=(TableXYDataset)vTimeSeriesCollection;
   vIntervalXYDataset=(IntervalXYDataset)vTimeSeriesCollection;

   vXYDataset=(XYDataset)vTimeSeriesCollection;
   /*
   vXYZDataset=(XYZDataset)vTimeSeriesCollection;
   //vWaferMapDataset=(WaferMapDataset)vTimeSeriesCollection;
   vWindDataset=(WindDataset)vTimeSeriesCollection;
   vOHLCDataset=(OHLCDataset)vTimeSeriesCollection;
   vSignalsDataset=(SignalsDataset)vTimeSeriesCollection;
   vBoxAndWhiskerXYDataset=(BoxAndWhiskerXYDataset)vTimeSeriesCollection;
   */
  }
  catch(Exception e)
  {}
 }
 public void createDataset(String strItemList,String strDataList)
 {
  vDefaultCategoryDataset=new DefaultCategoryDataset();
  vDefaultPieDataset=new DefaultPieDataset();

  strItemArray=strItemList.split(",");
  strDataArray=strDataList.split(",");
  for(int kIndex=0;kIndex<strItemArray.length;kIndex++)
  {
   vDefaultCategoryDataset.addValue(Double.parseDouble(strDataArray[kIndex])," ",strItemArray[kIndex]);
   vDefaultPieDataset.setValue(strItemArray[kIndex],Double.parseDouble(strDataArray[kIndex]));
  }
 }
 public DefaultCategoryDataset getDefaultCategoryDataset()
 {
  return this.vDefaultCategoryDataset;
 }
 public CategoryDataset getCategoryDataset()
 {
  return this.vCategoryDataset;
 }
 public DefaultPieDataset getDefaultPieDataset()
 {
  return this.vDefaultPieDataset;
 }
 public PieDataset getPieDataset()
 {
  return this.vPieDataset;
 }
 public XYDataset getXYDataset()
 {
  return this.vXYDataset;
 }
 public TableXYDataset getTableXYDataset()
 {
  return this.vTableXYDataset;
 }
 public XYZDataset getXYZDataset()
 {
  return this.vXYZDataset;
 }
 public IntervalXYDataset getIntervalXYDataset()
 {
  return this.vIntervalXYDataset;
 }
 public WindDataset getWindDataset()
 {
  return this.vWindDataset;
 }
 public WaferMapDataset getWaferMapDataset()
 {
  return this.vWaferMapDataset;
 }
 public IntervalCategoryDataset getIntervalCategoryDataset()
 {
  return this.vIntervalCategoryDataset;
 }
 public OHLCDataset getOHLCDataset()
 {
  return this.vOHLCDataset;
 }
 public SignalsDataset getSignalsDataset()
 {
  return this.vSignalsDataset;
 }
 public BoxAndWhiskerXYDataset getBoxAndWhiskerXYDataset()
 {
  return this.vBoxAndWhiskerXYDataset;
 }
 /*
 iChartType:ͼ������
 */
 public void createChart(int iChartType,String strFreeChartInfo,String strFreeChartXInfo,String strFreeChartYInfo)
 {
  switch(iChartType)
  {
   case 1:
    vFreeChart=ChartFactory.createPieChart(strFreeChartInfo,this.getPieDataset(),true,true,true);
    try
    {
     vPiePlot=(PiePlot)vFreeChart.getPlot();
     if(vPiePlot!=null)
     {
      if(bRender)
      {
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         //ָ��Sectionɫ��
         vPiePlot.setSectionPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         //ָ��Section��������ɫ
         vPiePlot.setSectionOutlinePaint(0,Color.BLACK);
        }
       }
      }
      //ָ��Section��ǩ��ʽ
      vStandardPieItemLabelGenerator=new StandardPieItemLabelGenerator("{1}");
      vPiePlot.setLabelGenerator(vStandardPieItemLabelGenerator);
      //
      vPiePlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 2:
    vFreeChart=ChartFactory.createPieChart(strFreeChartInfo,this.getPieDataset(),this.getPieDataset(),0,false,true,false,false,false,false);
    try
    {
     vPiePlot=(PiePlot)vFreeChart.getPlot();
     if(vPiePlot!=null)
     {
      if(bRender)
      {
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         //ָ��Sectionɫ��
         vPiePlot.setSectionPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         //ָ��Section��������ɫ
         vPiePlot.setSectionOutlinePaint(0,Color.BLACK);
        }
        //��ȡָ����
        vPiePlot.setExplodePercent(0,1.00);
       }
      }
      //ָ��Section��ǩ��ʽ
      vStandardPieItemLabelGenerator=new StandardPieItemLabelGenerator("{1}");
      vPiePlot.setLabelGenerator(vStandardPieItemLabelGenerator);

      vPiePlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 3:
    vFreeChart=ChartFactory.createMultiplePieChart(strFreeChartInfo,this.getCategoryDataset(),TableOrder.BY_ROW,true,false,false);
    try
    {
     vMultiplePiePlot=(MultiplePiePlot)vFreeChart.getPlot();
     if(vMultiplePiePlot!=null)
     {
      vFreeChartExtend=vMultiplePiePlot.getPieChart();
      vPiePlot=(PiePlot)vFreeChartExtend.getPlot();
      if(bRender)
      {
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         //ָ��Sectionɫ��
         vPiePlot.setSectionPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         //ָ��Section��������ɫ
         vPiePlot.setSectionOutlinePaint(0,Color.BLACK);
        }
       }
      }
      //ָ��Section��ǩ��ʽ
      vStandardPieItemLabelGenerator=new StandardPieItemLabelGenerator("{1}");
      vPiePlot.setLabelGenerator(vStandardPieItemLabelGenerator);
      vPiePlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 4:
    vFreeChart=ChartFactory.createPieChart3D(strFreeChartInfo,this.getPieDataset(),true,false,false);
    try
    {
     vPiePlot3D=(PiePlot3D)vFreeChart.getPlot();
     //vPiePlot3D.setForegroundAlpha(0.8f);
     if(vPiePlot3D!=null)
     {
      if(bRender)
      {
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         //ָ��Sectionɫ��
         vPiePlot3D.setSectionPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         //ָ��Section��������ɫ
         vPiePlot3D.setSectionOutlinePaint(0,Color.BLACK);
        }
       }
      }
      //ָ��Section��ǩ��ʽ
      vStandardPieItemLabelGenerator=new StandardPieItemLabelGenerator("{1}");
      vPiePlot3D.setLabelGenerator(vStandardPieItemLabelGenerator);
      //
      vPiePlot3D.setForegroundAlpha(0.8f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 5:
    vFreeChart=ChartFactory.createMultiplePieChart3D(strFreeChartInfo,this.getCategoryDataset(),TableOrder.BY_ROW,true,false,false);
    try
    {
     vMultiplePiePlot=(MultiplePiePlot)vFreeChart.getPlot();
    // vMultiplePiePlot.setForegroundAlpha(0.8f);
     if(vMultiplePiePlot!=null)
     {
      vFreeChartExtend=vMultiplePiePlot.getPieChart();
      vPiePlot3D=(PiePlot3D)vFreeChartExtend.getPlot();
      if(bRender)
      {
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         //ָ��Sectionɫ��
         vPiePlot3D.setSectionPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         //ָ��Section��������ɫ
         vPiePlot3D.setSectionOutlinePaint(0,Color.BLACK);
        }
       }
      }
      //ָ��Section��ǩ��ʽ
      vStandardPieItemLabelGenerator=new StandardPieItemLabelGenerator("{1}");
      vPiePlot3D.setLabelGenerator(vStandardPieItemLabelGenerator);
      //
      vPiePlot3D.setForegroundAlpha(0.8f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 6:
    vFreeChart=ChartFactory.createBarChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vBarRenderer=new BarRenderer();
       vBarRenderer.setBaseOutlinePaint(Color.GRAY);
       //����X������������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vBarRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vBarRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }
       vBarRenderer.setItemMargin(0.1);
       vCategoryPlot.setRenderer(vBarRenderer);
       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 7:
    vFreeChart=ChartFactory.createStackedBarChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vStackedBarRenderer=new StackedBarRenderer();
       vStackedBarRenderer.setBaseOutlinePaint(Color.GRAY);
       //����X������������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vStackedBarRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vStackedBarRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }

       vCategoryPlot.setRenderer(vStackedBarRenderer);
       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 8:
    vFreeChart=ChartFactory.createBarChart3D(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vBarRenderer3D=new BarRenderer3D();
       vBarRenderer3D.setBaseOutlinePaint(Color.GRAY);
       //����X������������ɫ
       if(strCategoryArray.length>0)
       {
       for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
       {
        vBarRenderer3D.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vBarRenderer3D.setSeriesOutlinePaint(0,Color.BLACK);
       }
      }
       vBarRenderer3D.setItemMargin(0.1);

       vCategoryPlot.setRenderer(vBarRenderer3D);
       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.8f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 9:
    vFreeChart=ChartFactory.createStackedBarChart3D(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vStackedBarRenderer3D=new StackedBarRenderer3D();
       vStackedBarRenderer3D.setBaseOutlinePaint(Color.GRAY);
       //����X������������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vStackedBarRenderer3D.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vStackedBarRenderer3D.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }

       vCategoryPlot.setRenderer(vStackedBarRenderer3D);

       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.8f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 10:
    vFreeChart=ChartFactory.createAreaChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getDefaultCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vStackedAreaRenderer=new StackedAreaRenderer();
       vStackedAreaRenderer.setBaseOutlinePaint(Color.GRAY);
       //����������ɫ
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         vStackedAreaRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         vStackedAreaRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }

       vCategoryPlot.setRenderer(vStackedAreaRenderer);

       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.5f);
      //���������
      vCategoryPlot.setDomainGridlinesVisible(true);
     }
    }
    catch(Exception e)
    {}
    break;
   case 11:
    vFreeChart=ChartFactory.createStackedAreaChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      if(bRender)
      {
       vStackedAreaRenderer=new StackedAreaRenderer();
       vStackedAreaRenderer.setBaseOutlinePaint(Color.GRAY);
       //����������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vStackedAreaRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vStackedAreaRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }

       vCategoryPlot.setRenderer(vStackedAreaRenderer);

       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 12:
    vFreeChart=ChartFactory.createLineChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      vLineAndShapeRenderer=new LineAndShapeRenderer();
      vLineAndShapeRenderer.setBaseOutlinePaint(Color.GRAY);
      if(bRender)
      {
       //����������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vLineAndShapeRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vLineAndShapeRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }

       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      vCategoryPlot.setRenderer(vLineAndShapeRenderer);
      //���������
      vCategoryPlot.setDomainGridlinesVisible(true);
     }
    }
    catch(Exception e)
    {}
    break;
   case 13:
    vFreeChart=ChartFactory.createLineChart3D(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      vLineRenderer3D=new LineRenderer3D();
      vLineRenderer3D.setBaseOutlinePaint(Color.GRAY);
      if(bRender)
      {
       //����������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vLineRenderer3D.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         //vLineRenderer3D.setSeriesFillPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vLineRenderer3D.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }
       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      vCategoryPlot.setRenderer(vLineRenderer3D);
      //���������
      vCategoryPlot.setDomainGridlinesVisible(true);
     }
    }
    catch(Exception e)
    {}
    break;
   case 14:
    vFreeChart=ChartFactory.createGanttChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getIntervalCategoryDataset(),true,false,false);
    break;
   case 15:
    vFreeChart=ChartFactory.createWaterfallChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getCategoryDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vCategoryPlot=vFreeChart.getCategoryPlot();
     if(vCategoryPlot!=null)
     {
      vWaterfallBarRenderer=new WaterfallBarRenderer();
      vWaterfallBarRenderer.setBaseOutlinePaint(Color.GRAY);
      if(bRender)
      {
       //��������ɫ
       if(strCategoryArray.length>0)
       {
        for(int iIndex=0;iIndex<strCategoryArray.length;iIndex++)
        {
         vWaterfallBarRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         //vWaterfallBarRenderer.setSeriesFillPaint(iIndex,new Color(0,0+iIndex*(255/strCategoryArray.length),255));
         vWaterfallBarRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }
       //����X�ᡢY�����ʾλ��
       vCategoryPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      vCategoryPlot.setRenderer(vWaterfallBarRenderer);
      //���������
      vCategoryPlot.setDomainGridlinesVisible(true);
      //��������͸����
      vCategoryPlot.setForegroundAlpha(0.5f);
     }
    }
    catch(Exception e)
    {}
    break;
   case 16:
    vFreeChart=ChartFactory.createPolarChart(strFreeChartInfo,this.getXYDataset(),true,false,false);
    break;
   case 17:
    vFreeChart=ChartFactory.createScatterPlot(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 18:
    vFreeChart=ChartFactory.createXYBarChart(strFreeChartInfo,strFreeChartXInfo,false,strFreeChartYInfo,this.getIntervalXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 19:
    vFreeChart=ChartFactory.createXYAreaChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 20:
    vFreeChart=ChartFactory.createStackedXYAreaChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getTableXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 21:
    vFreeChart=ChartFactory.createXYLineChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    try
    {
     vXYPlot=(XYPlot)vFreeChart.getXYPlot();
     if(vXYPlot!=null)
     {
      vXYItemRenderer=vXYPlot.getRenderer();
      vXYItemRenderer.setBaseOutlinePaint(Color.GRAY);
      if(bRender)
      {
       //����������ɫ
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         vXYItemRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         vXYItemRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }
       //����X�ᡢY�����ʾλ��
       vXYPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vXYPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
      }
      //����������ɫ
      vXYItemRenderer.setPaint(new Color(0,100,255));
      vXYPlot.setRenderer(vXYItemRenderer);
      //���������
      vXYPlot.setDomainGridlinesVisible(true);
     }
    }
    catch(Exception e)
    {}
    break;
   case 22:
    vFreeChart=ChartFactory.createXYStepChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 23:
    vFreeChart=ChartFactory.createXYStepAreaChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 24:
    vFreeChart=ChartFactory.createTimeSeriesChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYDataset(),true,false,false);
    try
    {
     vXYPlot=(XYPlot)vFreeChart.getXYPlot();
     if(vXYPlot!=null)
     {
      vXYItemRenderer=vXYPlot.getRenderer();
      vXYItemRenderer.setBaseOutlinePaint(Color.GRAY);
      if(bRender)
      {
       //����������ɫ
       if(strItemArray.length>0)
       {
        for(int iIndex=0;iIndex<strItemArray.length;iIndex++)
        {
         vXYItemRenderer.setSeriesPaint(iIndex,new Color(0,0+iIndex*(255/strItemArray.length),255));
         vXYItemRenderer.setSeriesOutlinePaint(0,Color.BLACK);
        }
       }
       //����X�ᡢY�����ʾλ��
       vXYPlot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
       vXYPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

       //����������ɫ
       vXYItemRenderer.setPaint(new Color(0,100,255));
       vXYPlot.setRenderer(vXYItemRenderer);
      }
      //���������
      vXYPlot.setDomainGridlinesVisible(true);

      SimpleDateFormat vSimpleDateFormat=null;
      if(strTimeAxis.equals("Hour"))
      {
       vSimpleDateFormat=new SimpleDateFormat("dd��HH��");
      }
      else
      {
       vSimpleDateFormat=new SimpleDateFormat("MM��dd��");
      }
           vDateAxis=(DateAxis)vXYPlot.getDomainAxis();
          vDateAxis.setDateFormatOverride(vSimpleDateFormat);
     }
    }
    catch(Exception e)
    {}
    break;
   case 25:
    vFreeChart=ChartFactory.createCandlestickChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getOHLCDataset(),true);
    break;
   case 26:
    vFreeChart=ChartFactory.createHighLowChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getOHLCDataset(),true);
    break;
   case 27:
    vFreeChart=ChartFactory.createHighLowChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getOHLCDataset(),true);
    break;
   case 28:
    vFreeChart=ChartFactory.createSignalChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getSignalsDataset(),true);
    break;
   case 29:
    vFreeChart=ChartFactory.createBubbleChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getXYZDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 30:
    vFreeChart=ChartFactory.createHistogram(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getIntervalXYDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   case 31:
    vFreeChart=ChartFactory.createBoxAndWhiskerChart(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getBoxAndWhiskerXYDataset(),true);
    break;
   case 32:
    vFreeChart=ChartFactory.createWindPlot(strFreeChartInfo,strFreeChartXInfo,strFreeChartYInfo,this.getWindDataset(),true,false,false);
    break;
   case 33:
    vFreeChart=ChartFactory.createWaferMapChart(strFreeChartInfo,this.getWaferMapDataset(),PlotOrientation.VERTICAL,true,false,false);
    break;
   default:
    vFreeChart=ChartFactory.createPieChart(strFreeChartInfo,this.getPieDataset(),true,false,false);
    break;
  }
  //ͼ������ɫ
  vFreeChart.setBackgroundPaint(new Color(212,234,243));
 }
 public JFreeChart getChart()
 {
  return this.vFreeChart;
 }
 public void createImageFile(int iWidth,int iHeight,HttpSession session)
 {
  try
  {
   java.util.Date vDate=new java.util.Date(System.currentTimeMillis());
   java.text.SimpleDateFormat vSimpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
   java.util.Date vFileDate=null;
   File[] vFileList=(new File(session.getServletContext().getRealPath("/"))).listFiles();
   for(int kIndex=0;kIndex<vFileList.length;kIndex++)
   {
    if(vFileList[kIndex].isFile())
    {
     if(vFileList[kIndex].getName().indexOf(".jpg")!=-1)
     {
      vFileDate=vSimpleDateFormat.parse(vFileList[kIndex].getName().substring(0,vFileList[kIndex].getName().length()-4));
      //ʮ����ǰ����ʱ�ļ�ɾ��
      if(vDate.getTime()-vFileDate.getTime()>60*10*1000)
      {
       vFileList[kIndex].delete();
      }
     }
    }
   }
   String strTime=vSimpleDateFormat.format(vDate);
   strFileName=session.getServletContext().getRealPath("/")+"\\"+strTime+".jpg";
   ChartUtilities.saveChartAsJPEG(new File(strFileName),100,this.getChart(),iWidth,iHeight);
   strFileName="/"+getApplicationName(session.getServletContext().getRealPath("/"))+"/"+strTime+".jpg";
  }
  catch(Exception e)
  {}
 }
 public String getImageFile()
 {
  return this.strFileName;
 }
 public String getApplicationName(String strRealPath)
 {
  String[] strAppArray=strRealPath.split("\\\\");
  return strAppArray[strAppArray.length-1];
 }
 public boolean getRender()
 {
  return bRender;
 }
 public void setRender(boolean bRender)
 {
  this.bRender=bRender;
 }
 public String getTimeAxis()
 {
  return this.strTimeAxis;
 }
 public void setTimeAxis(String strTimeAxis)
 {
  this.strTimeAxis=strTimeAxis;
 }
}



