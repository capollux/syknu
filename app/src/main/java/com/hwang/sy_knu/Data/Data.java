package com.hwang.sy_knu.Data;

public class Data {

	public static String[] Heads = new String[] { "학년","교과구분", "개설대학", "교과목 번호", "교과목 명", "학점", "강의", "실습","담당교수" ,"강의시긴","강의실","수강정원","수강신청","수강꾸러미신청","수강꾸러미 신청 가능 여부","비고"};
	
	public static String yearTerm;
	public static String seasonTerm;
	
	private String grade;
	private String subjectType;
	private String openMajor;
	private String subjectNum;
	private String subjectTitle;
	
	private String subjectUnitNum;
	private String subjectUnitLec;
	private String subjectUnitPra;
	private String professor;
	private String schedule;
	
	private String place;
	private String maxStu;
	private String currStu;
	private String resrStu;
	private String canResr;
	
	private String remark;
	
	public Data(String grade, String subjectType, String openMajor,
			String subjectNum, String subjectTitle, String subjectUnitNum,
			String subjectUnitLec, String subjectUnitPra, String professor,
			String schedule, String place, String maxStu, String currStu,
			String resrStu, String canResr, String remark) {
		super();
		this.grade = grade;
		this.subjectType = subjectType;
		this.openMajor = openMajor;
		this.subjectNum = subjectNum;
		this.subjectTitle = subjectTitle;
		
		this.subjectUnitNum = subjectUnitNum;
		this.subjectUnitLec = subjectUnitLec;
		this.subjectUnitPra = subjectUnitPra;
		this.professor = professor;
		this.schedule = schedule;
		
		this.place = place;
		this.maxStu = maxStu; //11
		this.currStu = currStu; //12
		this.resrStu = resrStu; //13
		this.canResr = canResr;
		
		this.remark = remark;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getOpenMajor() {
		return openMajor;
	}

	public void setOpenMajor(String openMajor) {
		this.openMajor = openMajor;
	}

	public String getSubjectNum() {
		return subjectNum;
	}

	public void setSubjectNum(String subjectNum) {
		this.subjectNum = subjectNum;
	}

	public String getSubjectTitle() {
		return subjectTitle;
	}

	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}

	public String getSubjectUnitNum() {
		return subjectUnitNum;
	}

	public void setSubjectUnitNum(String subjectUnitNum) {
		this.subjectUnitNum = subjectUnitNum;
	}

	public String getSubjectUnitLec() {
		return subjectUnitLec;
	}

	public void setSubjectUnitLec(String subjectUnitLec) {
		this.subjectUnitLec = subjectUnitLec;
	}

	public String getSubjectUnitPra() {
		return subjectUnitPra;
	}

	public void setSubjectUnitPra(String subjectUnitPra) {
		this.subjectUnitPra = subjectUnitPra;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getMaxStu() {
		return maxStu;
	}

	public void setMaxStu(String maxStu) {
		this.maxStu = maxStu;
	}

	public String getCurrStu() {
		return currStu;
	}

	public void setCurrStu(String currStu) {
		this.currStu = currStu;
	}

	public String getResrStu() {
		return resrStu;
	}

	public void setResrStu(String resrStu) {
		this.resrStu = resrStu;
	}

	public String getCanResr() {
		return canResr;
	}

	public void setCanResr(String canResr) {
		this.canResr = canResr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
	
}
