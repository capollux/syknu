package com.hwang.sy_knu.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailContext implements Parcelable {
	
	
	public static String[] Heads = new String[] { "과목명","과목번호", "학점", "개설대학", "개설학기",
		"교과구분", "담당교수", "강의시간","강의실" ,"연락처\nE-mail",
		"면담시간","강의언어","강의개요\n및 목적","교재 및\n참고문헌","강의진행\n방법 및\n활용 매체",
		"과제,\n평가방법,\n선수과목", "수강시\n참고사항","장애학생\n학습지원\n상황"};

	public static String[] Heads_Eng = new String[] { "Course\nTitle","Course\nCode", "Credits", "Department", "Semester",
		"Course\nCategories", "Instructor", "Hours","Location" ,"Phone/\nE-mail",
		"Office\nHours","language","Course\nGoals and\nObjectives","Textbook\nand other\nreferences","Course\nDescription,\nMethods,\nMaterials",
		"Assignment,\nGrading\nCriteria,\nPrerequisite\nSubject", "Notice To\nStudents","N.T.S\nwith\nDisabilities"};



	private String head;
	private String context;
	
	
	public DetailContext(String head, String context) {
		super();
		this.head = head;
		this.context = context;
	}

	private DetailContext(Parcel in) {
		this.head = in.readString();
		this.context = in.readString();
	}
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.head);
		dest.writeString(this.context);
	}

}
