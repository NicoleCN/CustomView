package com.example.customview.vector;

import android.view.View;
import android.widget.ImageView;

import com.example.customview.R;
import com.example.customview.base.BaseActivity;

/***
 * @date 2019-10-16 15:26
 * @author BoXun.Zhao
 * @description 研究不透彻
 */
public class VectorActivity extends BaseActivity {

    private boolean isSelected;

    //M：新建起点，参数x，y（M20， 30）
    //L：连接直线，参数x，y（L30， 20）
    //H：纵坐标不变，横向连线，参数x（H20）
    //V：横坐标不变，纵向连线，参数y（V30）
    //Q：二次贝塞尔曲线，参数x1，y1，x2，y2（Q10，20，30，40）
    //C：三次贝塞尔曲线，参数x1，y1，x2，y2，x3，y3（C10，20，30，40，50， 60）
    //Z：连接首尾，闭合曲线，无参数
    //个命令都有大小写形式，大写代表后面的参数是绝对坐标，小写表示相对坐标
    //动画效果 要注意矢量 位数相同 不然会 crash
    @Override
    protected int getLayoutId() {
        return R.layout.activity_vector_layout;
    }

    @Override
    protected void initView() {
        ImageView imageView = findViewById(R.id.vector_ImageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = !isSelected;
                imageView.setSelected(isSelected);
            }
        });
    }
}
