package com.chenssy.keyword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Description: ��ʼ�����дʿ⣬�����дʼ��뵽HashMap�У�����DFA�㷨ģ��
 * @Project��test
 * @version 1.0
 */
public class SensitiveWordInit {
	private String ENCODING = "GBK";    //�ַ�����
	@SuppressWarnings("rawtypes")
	public HashMap sensitiveWordMap;
	
	public SensitiveWordInit(){
		super();
	}
	
	@SuppressWarnings("rawtypes")
	public Map initKeyWord(){
		try {
			//��ȡ���дʿ�
			Set<String> keyWordSet = readSensitiveWordFile();
			//�����дʿ���뵽HashMap��
			addSensitiveWordToHashMap(keyWordSet);
			//spring��ȡapplication��Ȼ��application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * ��ȡ���дʿ⣬�����дʷ���HashSet�У�����һ��DFA�㷨ģ�ͣ�<br>
	 * �� = {
	 *      isEnd = 0
	 *      �� = {<br>
	 *      	 isEnd = 1
	 *           �� = {isEnd = 0
	 *                �� = {isEnd = 1}
	 *                }
	 *           ��  = {
	 *           	   isEnd = 0
	 *           		�� = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  �� = {
	 *      isEnd = 0
	 *      �� = {
	 *      	isEnd = 0
	 *      	�� = {
	 *              isEnd = 0
	 *              �� = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @param keyWordSet  ���дʿ�
	 * @version 1.0
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //��ʼ�����д��������������ݲ���
		String key = null;  
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//����keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //�ؼ���
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //ת����char��
				Object wordMap = nowMap.get(keyChar);       //��ȡ
				
				if(wordMap != null){        //������ڸ�key��ֱ�Ӹ�ֵ
					nowMap = (Map) wordMap;
				}
				else{     //���������򹹽�һ��map��ͬʱ��isEnd����Ϊ0����Ϊ���������һ��
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //�������һ��
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				
				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //���һ��
				}
			}
		}
	}

	/**
	 * ��ȡ���дʿ��е����ݣ���������ӵ�set������
	 * @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	private Set<String> readSensitiveWordFile() throws Exception{
		Set<String> set = null;
		
		File file = new File("D:\\SensitiveWord.txt");    //��ȡ�ļ�
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
		try {
			if(file.isFile() && file.exists()){      //�ļ����Ƿ����
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while((txt = bufferedReader.readLine()) != null){    //��ȡ�ļ������ļ����ݷ��뵽set��
					set.add(txt);
			    }
			}
			else{         //�������׳��쳣��Ϣ
				throw new Exception("���дʿ��ļ�������");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();     //�ر��ļ���
		}
		return set;
	}
}
