package pt.isel.leim.my_score.model

class Game(homeTeam:String, awayTeam:String, homeTeamScore:Int, awayTeamScore:Int, date:String, location:String, mvp:String, bestMoment:String, imageEncoded:String) {

    var homeTeamG:String = homeTeam
        get() = field
        set(value) { field = value }
    var awayTeamG:String = awayTeam
        get() = field
        set(value) { field = value }
    var homeTeamScoreG:Int = homeTeamScore
        get() = field
        set(value) { field = value }
    var awayTeamScoreG:Int = awayTeamScore
        get() = field
        set(value) { field = value }
    var dateG:String = date
        get() = field
        set(value) { field = value }
    var locationG:String = location
        get() = field
        set(value) { field = value }
    var mvpG:String = mvp
        get() = field
        set(value) { field = value }
    var bestMomentG:String = bestMoment
        get() = field
        set(value) { field = value }
    var imageEncodedG:String = imageEncoded
        get() = field
        set(value) { field = value }

}