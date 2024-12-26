import { NativeModules } from 'react-native';

export type createChannelRequest = {
    id: string;
    name: string;
}

export type channelId = string;

export type displayNotificationRequest = {
    title: string,
    body: string,
    android: {
        channelId: string
    }
}

const { NotificationService } = NativeModules;

export interface INotificationService {
    createChannel(request: createChannelRequest): Promise<channelId>;
    displayNotification(request: displayNotificationRequest): void;
}

export default NotificationService as INotificationService;