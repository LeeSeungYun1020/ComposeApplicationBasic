# Jetpack Compose 학습

## Jetpack Compose 개요

Android 최신 네이티브 선언적 UI 도구 키트
[(영상)](https://www.youtube.com/watch?v=7Mf2175h3RQ)  
빠르고 쉽다.
- 개발 가속화
- 적은 코드로 UI 구현
- 기능 구현에 집중

## 선언적 UI 도구 키트?
### 선언적  
동적 데이터와 실시간 업데이트가 잦은 요즘 어플리케이션.
어플리케이션 상태가 바뀔 때마다(ex. 사용자 입력 후 데이터베이스나 네트워크 호출) UI 업데이트가 필요하다. 뷰마다 상태가 다르고 각각 업데이트 해야하므로 과정이 복잡하며 버그가 많이 발생할 수 있다.  
제트팩 컴포즈에서는 상태를 UI로 변환한다. UI는 변경할 수 없고 한번 생성하면 업데이트가 불가능하다. 앱 상태가 바뀌면 새로운 상태를 새로운 표현으로 변환하여 UI 전체를 다시 생성하여 동기화 문제를 완전히 해결한다. 이 때 최적화를 위해 실제로는 변경되지 않은 요소에 대한 작업은 수행하지 않는다. 개념적으로는 특정 상태에 맞추어 UI를 새로 생성한다고 이해하면 된다.  
### 상태를 UI로 변환
컴포즈에서 UI 구성 요소는 `@Composable` 어노테이션이 달린 함수일 뿐이다. 따라서 함수를 이용하여 UI 구성 요소를 빠르고 쉽게 생성할 수 있다. 재사용 가능한 요소로 구성된 라이브러리로 UI를 나누는 것이 좋다. 함수는 값이 아닌 UI를 반환한다. Kotlin만을 이용하여 UI를 생성가능하다. Composable 함수는 데이터를 UI로 변환하기 때문에 매개변수를 받아야할 필요가 있다. 이런 방식으로 UI가 동기화 상태에서 벗어나지 않는다.
``` kotlin
@Composable
fun MessageList(messages: List<String>) {
    Column {
        if (messages.size == 0) {
            Text("No messages")
        } else {
            messages.forEach { message ->
                Text(text= message)
            }
        }
    }
}
```
예를 들어 위 코드에서 메시지 목록이 교체되어 상태가 변경되었을 때, `No messages`를 삭제하지 않는다거나 기존 메시지가 남아있는 문제는 발생하지 않는다. `MessageList` 함수가 실행되면 새 UI가 생성되기 때문이다. 이를 **리컴포징**이라 한다.  

``` kotlin
@Composable
fun MessageList(messages: List<String>) {
    Column {
        var selectAll: Boolean = ...
        Checkbox {
            checked = selectAll,
            onCheckChange = { checked -> 
                selectAll - checked
            }
        }
    }
}
```
정보를 입력할 때는 모두 매개변수로 컴포저블 함수에 전달해야 한다.
컴포저블 함수가 다이나믹이 될 수 없는 것은 아닌데 한 번 위 체크박스 예제를 살펴보자.
만약 `checked = false`만 작성할 경우 확인란을 클릭하여도 체크박스의 시각적인 변화는 없다.
이는 상태를 나타내는 상수를 전달하였기 때문이다.
상태를 변경하려면 매개변수를 전달해야 한다.
확인란 선택 여부를 결정하는 로컬변수 `selectAll`을 넣고 `onCheckChange` 콜백에서 로컬 상태를 업데이트하도록 구현해보자.
이렇게 하여 상태를 읽을 때 체크박스를 다시 받도록 할 수 있다.
콜백에서 상태를 바꾸지 않는 경우 확인란에 시각적인 변화가 없다는 사실을 아는 것이 중요하다.  
코드를 작성해야 확인란을 눌렀을 때 선택된다는 점에서 직관적이지 못하다고 생각할 수 있다.
그러나 이것이 선언적 UI의 핵심 개념이다.
요소는 전달되는 매개변수가 완전히 제어한다.
이렇게 단일 진실 소스(single source truth)를 생성하여 동기화할 대상을 없애버릴 수 있다.
확인란을 누르는 사용자에게 어떻게 반응할지는 개발자가 결정할 수 있다.
예를 들어 검증 과정 이후에 UI를 업데이트하려는 경우,
코드로 완전히 통제 가능하므로 검증이 실패한 시점에 다시 돌아와 이전 변경 사항을 취소할 필요가 없다.  
컴포즈는 입력 데이터가 바뀌면 컴포저블 함수를 다시 실행한다.
그러나 다시 실행하거나 리컴포지션을 호출할 때에도 유지하고 싶은 값이 있을 수 있다.
컴포저블 함수는 remember 함수를 사용하여 이전 실행에서 얻은 값을 기억할 수 있다.
``` kotlin
@Composable
fun MessageList(messages: List<String>) {
    Column {
        var selectAll by remember { mutableStateOf(false) }
        Checkbox {
            checked = selectAll,
            onCheckChange = { checked -> 
                selectAll = checked
            }
        }
    }
}
```
위 예시에서 `selectAll` 변수에 remember 함수를 사용하였다. 이를 통해 이전 실행에서 얻은 값을 기억하고 사용자가 체크하였는지를 저장할 수 있다. 
``` kotlin
@Composable
fun MessageList(
    messages: List<String>,
    selectAll: Boolean,
    onSelectAll: (Boolean) -> Unit
    ) {
    Column {
        Checkbox {
            checked = selectAll,
            onCheckChange = onSelectAll
        }
    }
}
```
또한 위 코드에서와 같이 state와 update 람다를 이용하여 컴포저블 함수의 매개변수로 전달하고
단일 진실 소스로 로직을 올릴 수 있다.(상위 함수에서 아래로 전달하도록 할 수 있다.)  

선언적 UI의 핵심은 특정 상태에서 UI의 형태를 완전하게 **설명**하고
상태가 바뀌면 프레임워크가 UI를 **업데이트**한다.  
Compose는 여러 가지 어플리케이션 아키텍처와 호환되지만
단방향 데이터 흐름을 따르는 아키텍처와 잘 맞다.
`ViewModel`이 `ScreenState`의 단일 스트림을 노출하면
Compose UI에서 관찰하여 각 구성 요소의 매개변수로 전달한다.
각 구성 요소는 필요한 상태만 수신하여 데이터가 변경되는 경우에만 업데이트할 수 있다.
`ViewState` 객체가 단일 스트림을 생성하면 상태 변경을 한 곳에서 처리하는데 도움을 준다.
전체 화면 상태를 추론할 수 있기 때문에 오류를 낮추기 쉬워진다.
이 패턴을 사용하여 간단하게 컴포저블 함수를 테스트할 수 있다.
이는 입력에 따라 컴포저블이 제어되기 때문이다.
## 선언적 패러다임 사용으로 UI를 쉽게 사용
컴포저블은 다양한 UI 구성 요소 도구 키트를 제공한다.
Jetpack Compose는 머터리얼 디자인 구성 요소와 테마 시스템을 구현하였다.
버튼, 카드, FAB, 앱바 같이 어플리케이션을 조립하는데 필요한 구성 요소를 제공한다.
모든 구성 요소가 기본적으로 머터리얼 디자인을 따르며 브랜드에 맞게 맞춤 설정 가능하다.
예를 들어 원하는 색, 도형, 서체 스타일을 적용할 수 있다.

### 행, 열, 박스
```kotlin
@Composable
fun MessageList() {
    Row {
        // 가로 Horizontal LinearLayout
    }

    Column {
        // 세로 Vertical LinearLayout
    }

    Box {
        // 박스 FrameLayout
    }
}
```

컴포즈는 간단하면서 강력한 새로운 레이아웃 시스템을 제공한다.
행과 열을 기반으로 하며 기존의 가로, 세로 선형 레이아웃과 비슷하다.
컴포즈는 뷰 시스템과 달리 여러 척도를 전달하지 못하도록 막아 중첩된 레이아웃을 효율적으로 만들 수 있다.  

### Constraint Layout
``` kotlin
ConstraintLayout {
    OutlinedAvatar(
        modifier = Modifier.constraintAs(avatar) {
            centerHorizontallyTo(parent)
            centerAround(image.bottom)
        }
    )
}
```

새로운 컴포즈 DSL을 적용한 ConstraintLayout을 사용하면
더욱 복잡한 레이아웃 표현이 가능하다.

### 맞춤형 레이아웃
맞춤형 레이아웃도 훨씬 간단하게 구현할 수 있다.  
``` kotlin
Layout (
    content = content,
    modifier = modifier
){ measurables, constraints ->
    layout(width, height) {
        placeables.forEach { placeable ->
            placeable.place(x, y)
        }
    }
}
```
위 코드와 같이 척도와 배치를 직접 설정하여 레이아웃을 만들 수도 있다.

### 애니메이션
가장 큰 개선 사항으로 새로운 애니메이션 시스템을 꼽을 수 있다.
``` kotlin
val radius by animateDpAsState(
    if (selected) 28.dp else 0.dp
)
Surface(
    shape= RoundedCornerShape (
        // 선택된 경우 시작 상단부를 28dp 둥글게 만든다.
        topStart = radius
    )
) {
    ...
}
```
보다 효과적이고 간단하게 UI에 모션을 적용할 수 있다.
컴포즈에 모션 레이아웃을 도입하는 것도 고려 중이므로 향후 업데이트시 사용 가능할 것으로 보인다.

### 테스트
컴포즈에서 테스트와 접근성이 일급 객체로 사용된다.
UI에 병렬 트리를 생성하는 시맨틱 시스템을 기반으로 동작한다.
접근성 서비스에 더 많은 정보를 제공하거나
UI 요소를 연결하여 어서션(assertion)하는데 도움이 된다.
컴포즈는 테스트 기능을 극대화할 수 있는 전용 테스트 아티팩트를 제공하며
독립적으로 컴포저블을 테스트하는 간편한 API를 제공한다.
테스트 규칙에서 clock이 노출된다.
그 다음으로 UI가 업데이트되고 이를 제어하기 위한 API가 제공된다.
애니메이션 코드 테스트시에도 테스트를 완전히 통제할 수 있다.

### 코루틴
``` kotlin
Box {
    modifier = Modifier.pointerInput {
        coroutineScope {
            while (true) {
                val offset = awaitPointerEventScope {
                    awaitFirstDown().position
                }
                launch {
                    animatedOffset.animateTo(offset)
                }
            }
        }
    }
}
```
코루틴(coroutine)을 사용하면 간단한 비동기식 API를 작성할 수 있다.
예를 들어 제스처, 애니메이션 스크롤링에 사용될 수 있다.
제스처를 애니메이션으로 넘어가는 것처럼 비동기식 이벤트를 결합한 코드를 간단하게 작성할 수 있다.
구조적 동시성을 통해 이런 취소와 정리 작업을 동시에 수행할 수 있다.
코틀린은 툴링으로 구성된 강력한 생태계를 갖추고 있다.
예를 들어 UI 구성요소를 새로운 함수로 추출해서 간단하게 재사용할 수 있다.

## 기능 활용 예시
### OWL
``` kotlin
@Composable
fun TopicChip(topic: Topic, selected: Boolean) {
    val radius by animateDpAsState (
        if (selected) 20.dp else 0.dp
    )
    Card(
        shape = RoundedCornerShape(
            topStart = radius
        )
    ) {
        Row {
            Box {
                Image(topic.image)
                if (selected) {
                    Icon(Icons.Filled.Done)
                }
            }
            
            Text(
                text = topic.name,
                modifier = Modifier.padding(16.dp))
        }
    }
    
}

@Composable
fun TopicsList (topic: List<Topic>) {
    // recyclerview 처럼 대용량 데이터에 적합
    LazyColumn(modifier = modifier) {
        items(topics) { topic ->
            TopicChip(topic)
        }
    }
}

Layout(
    content = content,
    modifier = modifier 
) { measurables, constraints ->
    // 계산 실행
    layout(width, height) {
        placeables.forEach { placeable ->
            placeable.place(x, y)
        }
    }
}

@Composable
fun TopicsScreen() {
    YellowTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.surface
        ) {
            TopicList()
        }
    }
}

@Composable
fun YellowTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        YellowThemeDark
    } else {
        YellowThemeLight
    }
    MaterialTheme(colors, content)
}
```

## 호환성
### 뷰와 상호운용
제트팩 컴포즈는 기존 뷰 시스템과 호환된다.
필요에 따라 화면 작은 요소부터 UI 큰 부분으로 점차 확대하여
전체 화면을 컴포즈롤 교체할 수 있다.
뷰에 컴포즈를 포함하거나 컴포즈에 뷰를 포함하는 방식 모두 사용 가능하다.

### 주요 라이브러리와 통합
기존 어플리케이션 아키텍처와 호환되어 처음부터 다시 시작할 필요가 없다.
예를 들어 Navigation, ViewModel, LiveData, Rx, Flow, Paging, Hilt와 호환된다.
Image loading 라이브러리는 래퍼를 제공하며 머터리얼 또는 AppCompat XML 테마를 Compose로 변환하는 어댑터를 제공한다.

## 개발 도구
강력한 도구와 완전한 문서가 없으면 개발자 환경이 갖추어졌다고 말하기 어렵다.
먼저 레이아웃 검사기 같이 친숙한 도구가 컴포즈를 지원하도록 업데이트하였다.
다음으로 효과적인 새로운 도구도 추가하였다.
컴포즈 프리뷰에서는 도구와 코드 간의 새로운 방식을 도입하여 구성 요소별로 별도의 미리 보기를 정의할 수 있도록 하였다.
컴포즈는 최신 도구 키트로 웨어러블을 비롯한 소형 폼팩터에서 테블릿과 크롬 북까지 다양한 화면을 지원한다.