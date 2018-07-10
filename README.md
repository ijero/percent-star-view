# percent-star-view
percent-star-view

XML:
```xml
<cn.ijero.psv.PercentStarView
    android:id="@+id/mainPercentStarView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="10dp"
    app:psv_isStrokeOverride="true"
    app:psv_max="100"
    app:psv_progress="15"
    app:psv_starBMI="0.5"
    app:psv_starBackColor="@color/colorPrimary"
    app:psv_starCount="5"
    app:psv_starProgressColor="@color/colorAccent"
    app:psv_starSize="50dp"
    app:psv_starStrokeColor="@color/colorPrimary"
    app:psv_starStrokeWidth="1px" />
```

JAVA:
```java
percentStarView.max(200)
            .progress(120)
            .bmi(0.3F)
            .count(4)
            .starBackColor(Color.WHITE)
            .starProgressColor(Color.RED)
            .starStrokeColor(Color.parseColor("#FF666666"))
            .starSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()))
            .strokeWidth(1)
            .strokeOverride(true);
```

