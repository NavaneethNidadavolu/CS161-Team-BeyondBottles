package com.example.alcholrehab.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alcholrehab.R
import com.example.alcholrehab.adapters.BlogAdapter
import com.example.alcholrehab.models.BlogItem

class BlogActivity : BaseActivity() {

    var listBlogs : ArrayList<BlogItem> = ArrayList()
    var blogElement : BlogItem? = null

    private lateinit var rvBlog : RecyclerView
    private lateinit var toolbarBlog : Toolbar
    private var mBlogAdapter : BlogAdapter? = null

    private var clicked : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        toolbarBlog = findViewById(R.id.toolbar_blog)
        setSupportActionBar(toolbarBlog)
        toolbarBlog.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        clicked = intent.extras!!.getInt("clicked")

        when (clicked) {
            1 -> {
                toolbarBlog.setTitle(R.string.before_rehab)
                navView.setCheckedItem(R.id.nav_before_rehab)
                addDataBeforeRehab()
            }
            2 -> {
                toolbarBlog.setTitle(R.string.during_rehab)
                navView.setCheckedItem(R.id.nav_during_rehab)
                addDataDuringRehab()
            }
            3 -> {
                toolbarBlog.setTitle(R.string.after_rehab)
                navView.setCheckedItem(R.id.nav_after_rehab)
                addDataAfterRehab()
            }
            else -> {
                toolbarBlog.setTitle(R.string.family_help)
                navView.setCheckedItem(R.id.nav_family_help)
                addDataFamily()
            }
        }


        rvBlog = findViewById(R.id.rv_blog)
//        val manager = Lin(this,3)
        rvBlog.layoutManager = LinearLayoutManager(this)

        mBlogAdapter = BlogAdapter()
        rvBlog.adapter = mBlogAdapter

        mBlogAdapter!!.setData(listBlogs)

    }

    private fun addDataBeforeRehab(){
        blogElement = BlogItem(0, "Is My Addiction Bad Enough?",
            "If your substance use interferes with your daily life, you should seek treatment.",
        "https://www.addictioncenter.com/rehab-questions/is-my-addiction-bad-enough/%20")
        listBlogs.add(0, blogElement!!)

        blogElement = BlogItem(1, "How Do I Pay For Treatment?",
            "Check with your insurance first, but there are programs to assist you",
        "https://www.addictioncenter.com/rehab-questions/paying-for-treatment/%20")
        listBlogs.add(1, blogElement!!)

        blogElement = BlogItem(2, "Does My Insurance Cover Rehab?",
            "Check our database to see if your provider is listed, if not, reach out to a treatment provider to learn about your options.",
        "https://www.addictioncenter.com/find-rehab-by-insurance-provider/%20")
        listBlogs.add(2, blogElement!!)

        blogElement = BlogItem(3, "How Do I Choose A Treatment Center?",
            "Investigate your options, ask questions, and determine which best suits your needs.",
        "https://www.addictioncenter.com/rehab-questions/choose-right-rehab/")
        listBlogs.add(3, blogElement!!)

        blogElement = BlogItem(4, "What Makes A Top-Rated Treatment Center?",
            "It depends on a multitude of factors such as a high success rate and comprehensive care.",
        "https://www.addictioncenter.com/rehab-questions/what-makes-a-top-rated-treatment-center/")
        listBlogs.add(4, blogElement!!)

        blogElement = BlogItem(5, "What's The Difference Between Inpatient And Outpatient Rehab?",
            "The biggest difference is whether you stay on-campus or not. Inpatient is residential while outpatient is not",
            "https://www.addictioncenter.com/treatment/inpatient-outpatient-rehab/")
        listBlogs.add(5, blogElement!!)

        blogElement = BlogItem(6, "Can I Do It On My Own?",
            "Detoxing on your own can cause severe withdrawal side effects.",
            "https://www.addictioncenter.com/rehab-questions/can-i-do-it-on-my-own/ ")
        listBlogs.add(6, blogElement!!)

        blogElement = BlogItem(7, "Should I Travel For Rehab?",
            "Traveling for rehab could provide you with just the right change of scenery.",
            "https://www.addictioncenter.com/rehab-questions/should-i-travel-for-rehab/")
        listBlogs.add(7, blogElement!!)

        blogElement = BlogItem(8, "How Do I Prepare For Rehab?",
            "Ensure all your responsibilities are covered in order to approach rehab with a clear mind.",
            "https://www.addictioncenter.com/rehab-questions/how-do-i-prepare-for-rehab/")
        listBlogs.add(8, blogElement!!)

        blogElement = BlogItem(9, "Why Does Rehab Have A Stigma?",
            "Despite contrary belief, addiction is not a choice, it is an illness that requires care.",
            "https://www.addictioncenter.com/rehab-questions/why-does-rehab-have-a-stigma/")
        listBlogs.add(9, blogElement!!)
    }

    private fun addDataDuringRehab(){
        blogElement = BlogItem(0, "What Is A Typical Day Like In Rehab?",
            "Usually days consist of meetings, therapy sessions, and a bit of free time.",
            "https://www.addictioncenter.com/rehab-questions/typical-day-rehab/")
        listBlogs.add(0, blogElement!!)

        blogElement = BlogItem(1, "How Long Does Detox Take?",
            "The length of detox depends on the substance that was being used but is usually between 2 to 7 days",
            "https://www.addictioncenter.com/rehab-questions/how-long-does-detox/")
        listBlogs.add(1, blogElement!!)

        blogElement = BlogItem(2, "How Long Does Treatment Take?",
            "It depends, but centers should have programs anywhere from 30-90 days",
            "https://www.addictioncenter.com/rehab-questions/how-long-does-treatment-take/ ")
        listBlogs.add(2, blogElement!!)

        blogElement = BlogItem(3, "What Can I Bring Into Treatment?",
            "Most centers will provide a comprehensive packing list to ensure you have the essentials.",
            "https://www.addictioncenter.com/rehab-questions/what-to-bring-to-rehab/")
        listBlogs.add(3, blogElement!!)

        blogElement = BlogItem(4, "What If I Need Help For My Mental Health Too?",
            "A dual diagnosis is when an individual has both a drug or alcohol addiction and a mental health condition",
            "https://www.addictioncenter.com/addiction/dual-diagnosis/")
        listBlogs.add(4, blogElement!!)

        blogElement = BlogItem(5, "What Will Happen To My Family?",
            "Ensure you’ve planned accordingly, and your family will be safe and sound while you’re away.",
            "https://www.addictioncenter.com/rehab-questions/what-about-my-family/")
        listBlogs.add(5, blogElement!!)


    }

    private fun addDataAfterRehab(){

        blogElement = BlogItem(0, "What Happens After Rehab?",
            "Using the tools you just learned, you readjust to a brand new life.",
            "https://www.addictioncenter.com/treatment/life-rehab/")
        listBlogs.add(0, blogElement!!)

        blogElement = BlogItem(1, "What Happens If I Relapse?",
            "Use the experience as a learning tool and remember why you’re seeking sobriety.",
            "https://www.addictioncenter.com/rehab-questions/what-happens-if-i-relapse/")
        listBlogs.add(1, blogElement!!)

        blogElement = BlogItem(2, "How Do I Stay Sober After Rehab?",
            "Experiment with new interests and understand that recovery is about the journey",
            "https://www.addictioncenter.com/rehab-questions/how-do-i-prepare-for-rehab/")
        listBlogs.add(2, blogElement!!)

        blogElement = BlogItem(3, "How Do I Handle Triggers",
            "Once you’re aware of what triggers you, you’ll know how to cope when they arise.",
            "https://www.addictioncenter.com/rehab-questions/how-handle-triggers/")
        listBlogs.add(3, blogElement!!)

        blogElement = BlogItem(4, "Can I Get My Job Back After Rehab?",
            "Yes, there are policies in place to ensure your employment.",
            "https://www.addictioncenter.com/rehab-questions/can-i-get-my-job-back/")
        listBlogs.add(4, blogElement!!)

        blogElement = BlogItem(5, "Will My Social Life Change After Rehab?",
            "Yes, but it might help to think of it as an opportunity for self-betterment",
            "https://www.addictioncenter.com/rehab-questions/will-my-social-life-change/")
        listBlogs.add(5, blogElement!!)

        blogElement = BlogItem(6, "How Do I Regain My Loved Ones’ Trust After Rehab?",
            "Patience, honesty, and intention will eventually prove your deserved trustworthiness",
            "https://www.addictioncenter.com/rehab-questions/regaining-loved-ones-trust/")
        listBlogs.add(6, blogElement!!)

    }

    private fun addDataFamily(){
        blogElement = BlogItem(0, "How Do I Help A Loved One With Addiction?",
            "You can stage an intervention with a professional, encourage rehabilitation options, or advocate for therapy.",
            "https://www.addictioncenter.com/addiction/your-loved-ones-and-addiction/")
        listBlogs.add(0, blogElement!!)

        blogElement = BlogItem(1, "How Does Addiction Affect The Family?",
            "Addiction can cause irreversible rifts in family structures; trust is lost, conflict is daily, and trauma is abundant",
            "https://www.addictioncenter.com/addiction/how-addiction-affects-the-family/")
        listBlogs.add(1, blogElement!!)

        blogElement = BlogItem(2, "How Do I Help A Recovering Alcoholic?",
            "By learning about addiction and recovery, you will understand the patience and process required in the journey.",
            "https://www.addictioncenter.com/rehab-questions/how-do-i-help-a-recovering-addict-or-alcoholic/")
        listBlogs.add(2, blogElement!!)

        blogElement = BlogItem(3, "How Do I Help A Loved One With An Opioid Addiction?",
            "Opioid addiction is devastating and dangerous; to help your loved one and your family, seek out family support groups.",
            "https://www.addictioncenter.com/addiction/family-support-opioid-addiction/")
        listBlogs.add(3, blogElement!!)

        blogElement = BlogItem(4, "What Is The Role Of Family In Addiction Recovery?",
            "Remain honest and open about the importance of treatment and be as supportive as possible.",
            "https://www.addictioncenter.com/addiction/role-family-addiction-recovery/")
        listBlogs.add(4, blogElement!!)

        blogElement = BlogItem(5, "How Is Family Therapy Helpful In Addiction Recovery?",
            "Family therapy can help mend the tears between members as well as contextualize everyone’s feelings and behaviors.",
            "https://www.addictioncenter.com/treatment/family-therapy/")
        listBlogs.add(5, blogElement!!)

        blogElement = BlogItem(6, "What Do I Do If My Loved One Refuses Treatment?",
            "here are several steps you can take after your loved one refuses to get help.",
            "https://www.addictioncenter.com/treatment/stage-intervention/10-steps-to-take-if-an-alcoholic-or-addict-refuses-treatment/")
        listBlogs.add(6, blogElement!!)

    }

}