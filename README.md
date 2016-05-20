# CollabNet-Story-Points-Automation
Description of the samples

DriverConnect: Connection Test Class

Let's start with "hello world" for the CTF SDK to test your environment setup and basic configuration settings. This program simply attempts a login to your CTF URL and prints a corresponding message.

RetrieveProjects: List of Projects 

RetrieveProjects obtains and displays the list of projects of which the user account passed in is an argument. When you use TeamForge through the web user interface, much of your work is tightly bound within a project. The SDK is no exception, and RetrieveProjects is a simple illustration of a common CTF coding technique -- traversing lists of projects accumulating or looking for data.

TrackerItems: Bulk Change 

Nearly every CTF SDK program will do some form of data manipulation. TrackerItems illustrates a very simple approach to implementing a "bulk change" capability.In this case, we're merely setting the priority of all tracker artifacts to 5, but many other more complex and useful SDK worflows will be variants of this basic recipe.

DocumentManager: Document Manager 

Many of the tools within CTF operate on file-based data. The document manager enables you to file away development related documents. The file release manager gives you a place to store your software deliverables, which are just files, after all. And every artifact in CTF can have one or more file attachments associated with it.

DocumentManager illustrates how to write a program which manipulates file-based data. The DocumentManager uploads a local file into your document manager, showing how simple it can be to work with files within CTF.

OverloadedUser: Overloaded Members 

Ever feel like you're the most overloaded member of your development team? If you use CTF with OverloadedUser, you'll know for sure! OverloadedUser shows a simple approach to finding your project member with the most hours of incompleted tasks allocated to him/her. This basic approach can be made more complex to provide a wide variety of task based statistics.

SCMCheckIn: Coding Guru

In the previous sample we used the SDK to find the user with the most assigned tasks. In the SCMCheckIn we take it a step further -- what team member is a "coding guru without parallel"? Who on your team submits the most code? Using the CTF SDK it's easy to find out since code submissions and their associated meta-data are modeled within the CTF database.