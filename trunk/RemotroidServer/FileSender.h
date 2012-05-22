#pragma once

#include "MyClient.h"
#include "afxcoll.h"
#include "TextProgressCtrl.h"

#define MAXDATASIZE		MAXSIZE-HEADERSIZE

class CFileSender
{
public:
	CFileSender();
	~CFileSender(void);

	void DeleteFileList(void);
	void SendFileData();		
	void SetProgressBar(CTextProgressCtrl * pProgressCtrl);
	BOOL AddSendFile(CFile * pFile);
	void SetClient(CMyClient *pClient);	
	BOOL StartSendFile(void);		
	static UINT SendFileThread(LPVOID pParam);

public:	
	CPtrList sendFileList;	
	BOOL isSending;	
	

private:
	CMyClient *m_pClient;
	HANDLE m_hSendFile;
	char buffer[MAXDATASIZE];
	unsigned long long sendedFileSize;
	unsigned long long totalFileSize;
	CWinThread *pSendFileThread;
	CTextProgressCtrl *m_progressCtrl;
	

private:
	int SendPacket(int iOPCode, const char * data, int iDataLen);	
	int SendFileInfo();
};

