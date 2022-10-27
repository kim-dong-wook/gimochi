import React, { useLayoutEffect } from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { getFocusedRouteNameFromRoute } from '@react-navigation/native';
import HomeScreen from '../screen/HomeScreen';
import AttendanceScreen from '../screen/AttendanceScreen';
import ChallengeScreen from '../screen/ChallengeScreen';
import PlayScreen from '../screen/PlayScreen';
import RollingpaperScreen from '../screen/RollingpaperScreen';
import ScheduleScreen from '../screen/ScheduleScreen';
import HomeModal from '../components/HomeModal';

const Home = createNativeStackNavigator();
function HomeNavigation({ route, navigation }) {
  useLayoutEffect(() => {
    const routeName = getFocusedRouteNameFromRoute(route);
    if (routeName === 'HomeModal') {
      //MyPage이외의 화면에 대해 tabBar none을 설정한다.
      navigation.setOptions({ tabBarStyle: { display: 'none' } });
    } else {
      navigation.setOptions({ tabBarStyle: { display: undefined } });
    }
    // console.log(routeName);
  }, [navigation, route]);

  // const routeName = getFocusedRouteNameFromRoute(route);
  return (
    <Home.Navigator
      // initialRouteName='HomeScreen'
      screenOptions={{
        headerTitleAlign: 'center',
        headerTitleStyle: {
          fontFamily: 'Regular',
        },
      }}
    >
      <Home.Screen name='HomeScreen' component={HomeScreen} options={{ title: '기모치' }}></Home.Screen>
      <Home.Screen
        name='AttendanceScreen'
        component={AttendanceScreen}
        options={{ title: '출석 체크' }}
      ></Home.Screen>
      <Home.Screen
        name='ChallengeScreen'
        component={ChallengeScreen}
        options={{ title: '챌린지콘' }}
      ></Home.Screen>
      <Home.Screen name='PlayScreen' component={PlayScreen} options={{ title: '놀이터' }}></Home.Screen>
      <Home.Screen
        name='RollingpaperScreen'
        component={RollingpaperScreen}
        options={{ title: '추카포카' }}
      ></Home.Screen>
      <Home.Screen
        name='ScheduleScreen'
        component={ScheduleScreen}
        options={{ title: '일정 관리' }}
      ></Home.Screen>
      <Home.Screen
        name='HomeModal'
        component={HomeModal}
        options={{
          presentation: 'transparentModal',
          headerShown: false,
          gestureEnabled: true,
        }}
      ></Home.Screen>
    </Home.Navigator>
  );
}

export default HomeNavigation;