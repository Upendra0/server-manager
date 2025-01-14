<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7 lt-ie10"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8 lt-ie10"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9 lt-ie10"> <![endif]-->
<!--[if IE 9]>         <html class="no-js lt-ie10"> <![endif]-->
<html>
	<jsp:include page="../common/newheader.jsp" ></jsp:include>
	
	<!-- Page-specific Plugin CSS: -->
    <link rel="stylesheet" href="styles/vendor/jquery.pnotify.default.css">
	<link rel="stylesheet" href="styles/vendor/select2/select2.css">
	
    <body class="dashboard-page">
    	<jsp:include page="../common/browserCompatibleCheck.jsp" ></jsp:include>
        
        <jsp:include page="../common/newleftMenu.jsp" ></jsp:include>
        
        <section class="wrapper scrollable">
            <jsp:include page="../common/newtopNavigationPanel.jsp" ></jsp:include>
            
            <section class="title-bar">
                <div>
                    <span>DASHBOARD</span>
                    <!-- <nav class="dashboard-menu">
                        <a href="javascript:;">
                            <i class="icon-cog toggle-widget-setup"></i>
                            <i class="menu-state-icon icon-sort-up"></i>
                            <i class="menu-state-icon icon-caret-down active"></i>
                        </a>
                        <ul>
                            
                            <li><a data-toggle="modal" href="#quickLaunchModal">Add Quick Launch Icon</a></li>
                            <li><a data-toggle="modal" href="#quickLaunchModal">Remove Quick Launch Icon</a></li>
                            <li><a href="javascript:;">Third Menu Item</a></li>
                        </ul>
                    </nav> -->
                </div>
            </section>
            <!-- <nav class="quick-launch-bar">
                <ul>
                    <li>
                        <a href="javascript:;">
                            <i class="icon-bar-chart"></i>
                            <span>Graphs</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="icon-calendar-empty"></i>
                            <span>Calendar</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="icon-map-marker"></i>
                            <span>Maps</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="icon-cloud"></i>
                            <span>Cloud</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;">
                            <i class="icon-microphone"></i>
                            <span>Conference</span>
                        </a>
                    </li>
                </ul>
                <a data-toggle="modal" href="#quickLaunchModal" class="add-quick-launch"><i class="icon-plus"></i></a>
            </nav> -->
			<section class="widget-group">
                <div class="proton-widget messages">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Location</label>
                                    <div>
                                        <select class="select2">
                                            <option selected="" value="Any">Any</option>
                                            <option value="Europe">Europe</option>
                                            <option value="Asia">Asia</option>
                                            <option value="North America">North America</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Time</label>
                                    <div>
                                        <select class="select2">
                                            <option>Any</option>
                                            <option>Last Hour</option>
                                            <option>Today</option>
                                            <option selected="">This Week</option>
                                            <option>This Month</option>
                                            <option>This Year</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-success front">
                        <div class="panel-heading">
                            <i class="icon-envelope-alt"></i>
                            <span>Messages</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <div>
                            <ul class="list-group pending">
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user1.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Nunc Cenenatis:
                                        </span>
                                        <span class="description-text">
                                            Hi, can you meet me at the office tomorrow morning?
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        5 mins ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user5.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Prasent Neque:
                                        </span>
                                        <span class="description-text">
                                            Just a quick question: do you know the balance on the adsense account?
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        17 mins ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user2.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Flor Demoa:
                                        </span>
                                        <span class="description-text">
                                            Hey, we're going surfing tomorrow. Feel free to join in.
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        3 hrs ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user1.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Nunc Cenenatis:
                                        </span>
                                        <span class="description-text">
                                            Did you remember to get the tickets?
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        1 day ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user4.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Morbi Demoa:
                                        </span>
                                        <span class="description-text">
                                            Great seeing you at the show yesterday.
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        2 days ago
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>



                <div class="proton-widget general-stats">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Location</label>
                                    <div>
                                        <select class="select2">
                                            <option value="Any">Any</option>
                                            <option selected="" value="Europe">Europe</option>
                                            <option value="Asia">Asia</option>
                                            <option value="North America">North America</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Time</label>
                                    <div>
                                        <select class="select2">
                                            <option>Any</option>
                                            <option>Last Hour</option>
                                            <option>Today</option>
                                            <option>This Week</option>
                                            <option>This Month</option>
                                            <option selected="">This Year</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-primary front">
                        <div class="panel-heading">
                            <i class="icon-sort"></i>
                            <span>General Stats</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="text-holder">
                                    <span class="title-text">
                                        2,511
                                    </span>
                                    <span class="description-text">
                                        Registered Users
                                    </span>
                                </div>
                                <span class="stat-value">
                                    + 0.6%
                                    <i class="icon-sort-up"></i>
                                </span>
                            </li>
                            <li class="list-group-item">
                                <div class="text-holder">
                                    <span class="title-text">
                                        $1,132
                                    </span>
                                    <span class="description-text">
                                        Revenue
                                    </span>
                                </div>
                                <span class="stat-value">
                                    + 2.1%
                                    <i class="icon-sort-up"></i>
                                </span>
                            </li>
                            <li class="list-group-item">
                                <div class="text-holder">
                                    <span class="title-text">
                                        53
                                    </span>
                                    <span class="description-text">
                                        Viking Users
                                    </span>
                                </div>
                                <span class="stat-value">
                                    0%
                                    <i class="icon-sort"></i>
                                </span>
                            </li>
                            <li class="list-group-item">
                                <div class="text-holder">
                                    <span class="title-text">
                                        24
                                    </span>
                                    <span class="description-text">
                                        Donuts consumed
                                    </span>
                                </div>
                                <span class="stat-value">
                                    - 6.5%
                                    <i class="icon-sort-down"></i>
                                </span>
                            </li>
                            <li class="list-group-item">
                                <div class="text-holder">
                                    <span class="title-text">
                                        312
                                    </span>
                                    <span class="description-text">
                                        Orders Received
                                    </span>
                                </div>
                                <span class="stat-value">
                                    + 12.1%
                                    <i class="icon-sort-up"></i>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>



                <div class="proton-widget latest-users">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Location</label>
                                    <div>
                                        <select class="select2">
                                            <option selected="" value="Any">Any</option>
                                            <option value="Europe">Europe</option>
                                            <option value="Asia">Asia</option>
                                            <option value="North America">North America</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-info front">
                        <div class="panel-heading">
                            <i class="icon-flag"></i>
                            <span>Latest Users</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <div>
                            <ul class="list-group pending">
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user3.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Etiam Libero
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        a minut ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user2.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Flor Demoa
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        12 hours ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user5.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Prasent Neque
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        2 days ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user1.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Nunc Cenenatis
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        5 days ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user4.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Morbi Demoa
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        11 days ago
                                    </span>
                                </li>
                                <li class="list-group-item">
                                    <i><img src="img/user-icons/user2.jpg" alt="User Icon"></i>
                                    <div class="text-holder">
                                        <span class="title-text">
                                            Sel Drvenar
                                        </span>
                                    </div>
                                    <span class="stat-value">
                                        11 days ago
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>



                <div class="proton-widget task-completion">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Origin</label>
                                    <div>
                                        <select class="select2">
                                            <option value="Any">Any</option>
                                            <option value="Europe">Europe</option>
                                            <option value="Asia">Asia</option>
                                            <option selected="" value="North America">North America</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Time</label>
                                    <div>
                                        <select class="select2">
                                            <option>Any</option>
                                            <option>Last Hour</option>
                                            <option>Today</option>
                                            <option>This Week</option>
                                            <option selected="">This Month</option>
                                            <option>This Year</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-info front">
                        <div class="panel-heading">
                            <i class="icon-ok"></i>
                            <span>Task Completion</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <ul class="list-group">
                            <li class="sub-list">
                                <ul>
                                    <li class="list-group-item">
                                        <span class="title-text">
                                            Processed orders
                                        </span>
                                        <span class="processed-value">
                                            56
                                        </span>
                                    </li>
                                    <li class="list-group-item">
                                        <span class="title-text">
                                            Pending orders
                                        </span>
                                        <span class="processed-value">
                                            14
                                        </span>
                                    </li>
                                    <li class="list-group-item">
                                        <span class="title-text">
                                            Unproc. orders
                                        </span>
                                        <span class="processed-value">
                                            12
                                        </span>
                                    </li>
                                </ul>
                            </li>
                            <li class="widget-progress-bar">
                                <div class="form-group">
                                    <label>Processed orders: 63%</label>
                                    <div class="progress">
                                        <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="63" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                                            <span class="sr-only">63% Complete</span>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="proton-widget">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Products</label>
                                    <div>
                                        <select class="select2">
                                            <option>All</option>
                                            <option selected="">Digital Media</option>
                                            <option>Books</option>
                                            <option>Shopping Carts</option>
                                            <option>Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Origin</label>
                                    <div>
                                        <select class="select2">
                                            <option value="Any">Any</option>
                                            <option value="Europe">Europe</option>
                                            <option value="Asia">Asia</option>
                                            <option selected="" value="North America">North America</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Time Unit</label>
                                    <div>
                                        <select class="select2">
                                            <option>Day</option>
                                            <option>Week</option>
                                            <option>Month</option>
                                            <option selected="">Year</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-warning front">
                        <div class="panel-heading">
                            <i class="icon-shopping-cart"></i>
                            <span>Sales Income</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <div>
                            <div class="form-group">
                                <div id="hero-bar" class="graph" style="height: 225px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="proton-widget">
                    <div class="panel panel-default back">
                        <div class="panel-heading">
                            <i class="icon-cog"></i>
                            <span>Settings</span>
                            <div class="toggle-widget-setup">
                                <i class="icon-ok"></i>
                                <span>DONE</span>
                            </div>
                        </div>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Division</label>
                                    <div>
                                        <select class="select2">
                                            <option selected="" value="All">All Divisions</option>
                                            <option value="RnD">R&amp;D</option>
                                            <option value="Production">Production</option>
                                            <option value="Marketing">Marketing</option>
                                            <option value="Sales">Sales</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Products</label>
                                    <div>
                                        <select class="select2">
                                            <option selected="">All</option>
                                            <option>Digital Media</option>
                                            <option>Books</option>
                                            <option>Shopping Carts</option>
                                            <option>Other</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li class="list-group-item">
                                <div class="form-group">
                                    <label>Filter by Time</label>
                                    <div>
                                        <select class="select2">
                                            <option>Any</option>
                                            <option>This Week</option>
                                            <option>This Month</option>
                                            <option selected="">This Year</option>
                                            <option>Ten Years</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel panel-danger front">
                        <div class="panel-heading">
                            <i class="icon-chevron-sign-down"></i>
                            <span>Expenses</span>
                            <i class="icon-cog toggle-widget-setup"></i>
                        </div>
                        <div>
                            <div class="form-group">
                                <div id="hero-donut" class="graph" style="margin-top: 10px; height: 185px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </section>

        <div id="quickLaunchModal" tabindex="-1" role="dialog" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><i class="icon-plus"></i><span>EDIT QUICK LAUNCH ITEMS</span></h4>
                    </div>
                    <div class="modal-body">
                        <p>Edit quick launch items below:</p>
                        <input type="text" value="Graphs,Calendar,Maps,Cloud,Conference" data-role="tagsinput" />
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-lg btn-default" data-dismiss="modal">CANCEL</button>
                        <button type="button" class="btn btn-lg btn-success" data-dismiss="modal">SAVE SELECTION</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>

		<!-- Page-specific scripts: -->
        <script src="jquery/1.10.3/jquery-ui.min.js"></script>
        
		<jsp:include page="../common/newfooter.jsp" ></jsp:include> 
        
        <script src="scripts/proton/73f27b75.dashboard.js"></script>
        <script src="scripts/proton/217183f0.dashdemo.js"></script>
    </body>
</html>
